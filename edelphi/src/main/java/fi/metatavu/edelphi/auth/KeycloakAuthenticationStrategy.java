package fi.metatavu.edelphi.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.edelphi.Defaults;
import fi.metatavu.edelphi.auth.api.KeycloakApi;
import fi.metatavu.edelphi.auth.api.KeycloakBrokerToken;
import fi.metatavu.edelphi.smvcj.controllers.RequestContext;
import fi.metatavu.edelphi.utils.AuthUtils;
import net.sf.json.JSONObject;

public class KeycloakAuthenticationStrategy extends OAuthAuthenticationStrategy {
  
  private static final Logger logger = Logger.getLogger(KeycloakAuthenticationStrategy.class.getName());

  @Override
  public String getName() {
    return "Keycloak";
  }

  @Override
  protected String getApiKey() {
    return settings.get("oauth.keycloak.apiKey");
  }

  @Override
  protected String getApiSecret() {
    return settings.get("oauth.keycloak.apiSecret");
  }

  @Override
  protected String getOAuthCallbackURL(RequestContext requestContext) {
    return getCallbackUrl();
  }
  
  public String getAccountUrl() {
    return String.format("%s/realms/%s/account?referrer=%s", getServerUrl(), getRealm(), getApiKey());
  }
  
  private String getLogoutUrl(String redirectUrl) {
    try {
      return String.format("%s/realms/%s/protocol/openid-connect/logout?redirect_uri=%s", getServerUrl(), getRealm(), URLEncoder.encode(redirectUrl, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      logger.log(Level.SEVERE, "Failed to encode Keycloak logout URL", e);
      return null;
    }
  }

  private String getTokenUrl(String broker) {
    return String.format("%s/realms/%s/broker/%s/token", getServerUrl(), getRealm(), broker   );
  }
  
  @Override
  public void logout(RequestContext requestContext, String redirectUrl) {
    requestContext.setRedirectURL(getLogoutUrl(redirectUrl));
  }
  
  @Override
  protected Api getApi(Map<String, String> apiParams) {
    String hint = apiParams != null ? apiParams.get("hint") : null;
    String locale = apiParams != null ? apiParams.get("locale") : null;
    if (StringUtils.isBlank(locale)) {
      locale = Defaults.LOCALE;
    }
    
    return new KeycloakApi(getServerUrl(), getRealm(), hint, locale);
  }

  @Override
  protected AuthenticationResult processResponse(RequestContext requestContext, OAuthService service, String[] requestedScopes) {
    String code = requestContext.getString("code");

    Verifier verifier = new Verifier(code);
    Token accessToken = service.getAccessToken(null, verifier);

    Date expiresAt = getExpiresAt(accessToken);
    String refreshToken = getRefreshToken(accessToken);
    
    Response response = doSignedGet(accessToken, getUserinfoUrl());
    
    ObjectMapper objectMapper = new ObjectMapper();
    try (InputStream stream = response.getStream()) {
      UserInfo userInfo = objectMapper.readValue(stream, UserInfo.class);
      String externalId = userInfo.getSub();
      List<String> emails = Arrays.asList(userInfo.getEmail());
      String firstName = userInfo.getGivenName();
      String lastName = userInfo.getFamilyName();

      AuthUtils.storeOAuthAccessToken(requestContext, getName(), new OAuthAccessToken(externalId, accessToken.getToken(), refreshToken, expiresAt, requestedScopes));
      
      return processExternalLogin(requestContext, externalId, emails, firstName, lastName);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to read user info", e);
      return null;
    }
  }
  
  /**
   * Refreshes the access token
   * 
   * @param requestContext request context
   * @param token token
   * @return refreshed token
   */
  public OAuthAccessToken refreshToken(RequestContext requestContext, OAuthAccessToken token) {
    OAuthAccessToken result = token;
    
    String[] scopes = token.getScopes();
    String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", getServerUrl(), getRealm());
    
    HttpPost httpPost = new HttpPost(tokenUrl);
    
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("client_id", getApiKey()));
      params.add(new BasicNameValuePair("client_secret", getApiSecret()));
      params.add(new BasicNameValuePair("grant_type", "refresh_token"));
      params.add(new BasicNameValuePair("refresh_token", token.getRefreshToken()));

      httpPost.setEntity(new UrlEncodedFormEntity(params));
      try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
        StatusLine statusLine = response.getStatusLine();
        
        if (statusLine.getStatusCode() == 200) {
          HttpEntity entity = response.getEntity();
          
          ObjectMapper objectMapper = new ObjectMapper();
          try (InputStream stream = entity.getContent()) {
            RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(stream, RefreshTokenResponse.class);
            Date expiresAt = getExpiresAt(refreshTokenResponse.getExpiresIn());
            result = new OAuthAccessToken(token.getExternalId(), refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken(), expiresAt, scopes);
            AuthUtils.purgeOAuthAccessTokens(requestContext, getName());
            AuthUtils.storeOAuthAccessToken(requestContext, getName(), result);            
          }
          
          EntityUtils.consume(entity);
        } else {
          logger.log(Level.WARNING, String.format("Failed to refresh access token with message [%d]: %s", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
        }
      }
    } catch (IOException e) {
      logger.log(Level.WARNING, "Failed to refresh access token", e);
    }
    
    return result;
  }
  
  public OAuthAccessToken getBrokerToken(RequestContext requestContext, String broker) {
    OAuthAccessToken storedBrokerToken = AuthUtils.getOAuthAccessToken(requestContext, broker);
    if (storedBrokerToken != null) {
      return storedBrokerToken;
    }
    
    OAuthAccessToken keycloakToken = AuthUtils.getOAuthAccessToken(requestContext, getName());
    if (keycloakToken == null) {
      return null;
    }
    
    String url = getTokenUrl(broker);
    Response response = doSignedGet(keycloakToken, url);
    ObjectMapper objectMapper = new ObjectMapper();
    OAuthAccessToken resolvedBrokerToken = null;
    
    if (!response.isSuccessful()) {
      return null;
    }
    
    try (InputStream stream = response.getStream()) {
      KeycloakBrokerToken brokerToken = objectMapper.readValue(stream, KeycloakBrokerToken.class);
      Date expiresAt = getExpiresAt(brokerToken.getExpiresIn());
      resolvedBrokerToken = new OAuthAccessToken(null, brokerToken.getAccessToken(), null, expiresAt, null);
      AuthUtils.storeOAuthAccessToken(requestContext, broker, resolvedBrokerToken);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to process broker token response", e);
    }

    return resolvedBrokerToken;
  }
  
  @Override
  public String[] getKeys() {
    return new String[] { "oauth.keycloak.apiKey", "oauth.keycloak.apiSecret", "oauth.keycloak.callbackUrl" };
  }

  @Override
  public String localizeKey(Locale locale, String key) {
    return key;
  }

  private Response doSignedGet(OAuthAccessToken accessToken, String url) {
    return doSignedGet(accessToken.getToken(), url);
  }
  
  private Response doSignedGet(Token accessToken, String url) {
    return doSignedGet(accessToken.getToken(), url);
  }

  private Response doSignedGet(String accessToken, String url) {
    OAuthRequest request = new OAuthRequest(Verb.GET, url);
    request.addHeader("Authorization", String.format("Bearer %s", accessToken));
    return request.send();
  }
  
  private Date getExpiresAt(Token accessToken) {
    JSONObject rawJson = JSONObject.fromObject(accessToken.getRawResponse());
    int expiresIn = rawJson.getInt("expires_in");
    return getExpiresAt(expiresIn);
  }

  /**
   * Parses refresh token from the access token
   * 
   * @param accessToken access token
   * @return refresh token
   */
  private String getRefreshToken(Token accessToken) {
    JSONObject rawJson = JSONObject.fromObject(accessToken.getRawResponse());
    return rawJson.getString("refresh_token");
  }
  
  private Date getExpiresAt(int expiresIn) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    calendar.add(Calendar.SECOND, expiresIn);
    return calendar.getTime();
  }
  
  private String getCallbackUrl() {
    return settings.get("oauth.keycloak.callbackUrl");
  }
  
  private String getServerUrl() {
    return settings.get("oauth.keycloak.serverUrl");
  }
  
  private String getRealm() {
    return settings.get("oauth.keycloak.realm");
  }
  
  private String getUserinfoUrl() {
    return String.format("%s/realms/%s/protocol/openid-connect/userinfo", getServerUrl(), getRealm());
  }

  /**
   * Keycloak refresh token end-point result
   * 
   * @author Antti Leppä
   */
  @JsonIgnoreProperties (ignoreUnknown = true)
  public static class RefreshTokenResponse {
    
    @JsonProperty ("access_token")
    private String accessToken;
    
    @JsonProperty ("expires_in")
    private Integer expiresIn;

    @JsonProperty ("refresh_expires_in")
    private Integer refreshExpiresIn;

    @JsonProperty ("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
      return accessToken;
    }

    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
      return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
      this.expiresIn = expiresIn;
    }

    public Integer getRefreshExpiresIn() {
      return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
      this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getRefreshToken() {
      return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
    }
  }
  
  @JsonIgnoreProperties (ignoreUnknown = true)
  public static class UserInfo {
    private String sub;

    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String email;

    public String getSub() {
      return sub;
    }

    public void setSub(String sub) {
      this.sub = sub;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPreferredUsername() {
      return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
      this.preferredUsername = preferredUsername;
    }

    public String getGivenName() {
      return givenName;
    }

    public void setGivenName(String givenName) {
      this.givenName = givenName;
    }

    public String getFamilyName() {
      return familyName;
    }

    public void setFamilyName(String familyName) {
      this.familyName = familyName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

  }
}
