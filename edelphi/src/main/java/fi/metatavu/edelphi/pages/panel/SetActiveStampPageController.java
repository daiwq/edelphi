package fi.metatavu.edelphi.pages.panel;

import fi.metatavu.edelphi.domainmodel.features.Feature;
import fi.metatavu.edelphi.smvcj.controllers.PageRequestContext;
import fi.metatavu.edelphi.utils.RequestUtils;

public class SetActiveStampPageController extends PanelPageController {

  public SetActiveStampPageController() {
    super();
  }
  
  @Override
  public Feature getFeature() {
    return Feature.MANAGE_PANEL_TIMESTAMPS;
  }
  
  @Override
  public void processPageRequest(PageRequestContext pageRequestContext) {
    RequestUtils.setActiveStamp(pageRequestContext, pageRequestContext.getLong("stampId"));
    String url = pageRequestContext.getRequest().getHeader("Referer");
    pageRequestContext.setRedirectURL(url);
  }
  
}
