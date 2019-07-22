package fi.metatavu.edelphi.reports.text.batch;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import fi.metatavu.edelphi.batch.JobProperty;
import fi.metatavu.edelphi.batch.TypedItemProcessor;
import fi.metatavu.edelphi.domainmodel.panels.PanelStamp;
import fi.metatavu.edelphi.domainmodel.querylayout.QueryPage;
import fi.metatavu.edelphi.panels.PanelController;
import fi.metatavu.edelphi.reports.ReportException;
import fi.metatavu.edelphi.reports.text.TextReportController;
import fi.metatavu.edelphi.reports.text.TextReportPageContext;

/**
 * Generates HTML reports for given query pages
 * 
 * @author Antti Leppä
 */
@Named
@Stateless
@AccessTimeout (unit = TimeUnit.HOURS, value = 4)
public class TextReportPageHtmlProcessor extends TypedItemProcessor<QueryPage, String> {

  @Inject
  private PanelController panelController;

  @Inject
  private TextReportController htmlReportController;
  
  @Inject
  @JobProperty
  private String baseUrl;
  
  @Inject
  @JobProperty
  private Locale locale;

  @Inject
  @JobProperty
  private Long stampId;
  
  @Override
  public String process(QueryPage queryPage) throws Exception {
    PanelStamp stamp = panelController.findPanelStampById(stampId);
    if (stamp == null) {
      throw new ReportException(String.format("Could not find panel stamp %d", stampId));
    }

    // TODO: filters and parameters...
    Map<String, List<String>> filters = Collections.emptyMap();
    Map<String, String> parameters = Collections.emptyMap();
    
    return htmlReportController.getPageHtml(new TextReportPageContext(baseUrl, locale, stamp, filters, parameters, queryPage));
  }

}