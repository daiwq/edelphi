package fi.metatavu.edelphi.reports.image;

import java.util.Locale;

import fi.metatavu.edelphi.domainmodel.panels.PanelStamp;
import fi.metatavu.edelphi.domainmodel.querylayout.QueryPage;

/**
 * Image report page context
 * 
 * @author Antti Leppä
 */
public class ImageReportPageContext {

  private String baseURL;
  private Locale locale;
  private PanelStamp stamp;
  private Long[] expertiseGroupIds;
  private Long[] queryReplyIds;
  private QueryPage page;

  /**
   * Zero-argument constructor
   */
  public ImageReportPageContext() {
    super();
  }
  
  /**
   * Constructor
   * 
   * @param baseURL application base URL
   * @param locale locale
   * @param stamp panel stamp
   * @param expertiseGroupIds expertise group ids
   * @param page query page
   */
  public ImageReportPageContext(String baseURL, Locale locale, PanelStamp stamp, Long[] expertiseGroupIds, Long[] queryReplyIds, QueryPage page) {
    super();
    this.baseURL = baseURL;
    this.locale = locale;
    this.stamp = stamp;
    this.expertiseGroupIds = expertiseGroupIds;
    this.queryReplyIds = queryReplyIds;
    this.page = page;
  }

  /**
   * Returns application base URL
   * 
   * @return base URL
   */
  public String getBaseURL() {
    return baseURL;
  }

  /**
   * Sets application base URL
   * 
   * @param baseURL base URL
   */
  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  /**
   * Returns locale
   * 
   * @return locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Sets locale
   * 
   * @param locale locale
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Returns panel stamp
   * 
   * @return panel stamp
   */
  public PanelStamp getStamp() {
    return stamp;
  }

  /**
   * Sets panel stamp
   * 
   * @param stamp stamp
   */
  public void setStamp(PanelStamp stamp) {
    this.stamp = stamp;
  }
  
  /**
   * Returns expertise group ids
   * 
   * @return expertise group ids
   */
  public Long[] getExpertiseGroupIds() {
    return expertiseGroupIds;
  }
  
  /**
   * Sets expertise group ids.
   * 
   * @param expertiseGroupIds expertise group ids
   */
  public void setExpertiseGroupIds(Long[] expertiseGroupIds) {
    this.expertiseGroupIds = expertiseGroupIds;
  }
  
  /**
   * Returns query reply ids
   * 
   * @return query reply ids
   */
  public Long[] getQueryReplyIds() {
    return queryReplyIds;
  }
 
  /**
   * Sets query reply ids
   * 
   * @param queryReplyIds query reply ids
   */
  public void setQueryReplyIds(Long[] queryReplyIds) {
    this.queryReplyIds = queryReplyIds;
  }

  /**
   * Returns query page
   * 
   * @return page
   */
  public QueryPage getPage() {
    return page;
  }

  /**
   * Sets query page 
   * 
   * @param page page
   */
  public void setPage(QueryPage page) {
    this.page = page;
  }

}
