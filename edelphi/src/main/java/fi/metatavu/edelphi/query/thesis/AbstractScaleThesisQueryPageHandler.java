package fi.metatavu.edelphi.query.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.edelphi.dao.querydata.QueryQuestionOptionAnswerDAO;
import fi.metatavu.edelphi.dao.querymeta.QueryFieldDAO;
import fi.metatavu.edelphi.dao.querymeta.QueryOptionFieldDAO;
import fi.metatavu.edelphi.dao.querymeta.QueryOptionFieldOptionDAO;
import fi.metatavu.edelphi.domainmodel.querydata.QueryQuestionMultiOptionAnswer;
import fi.metatavu.edelphi.domainmodel.querydata.QueryQuestionOptionAnswer;
import fi.metatavu.edelphi.domainmodel.querydata.QueryReply;
import fi.metatavu.edelphi.domainmodel.querylayout.QueryPage;
import fi.metatavu.edelphi.domainmodel.querymeta.QueryOptionField;
import fi.metatavu.edelphi.domainmodel.querymeta.QueryOptionFieldOption;
import fi.metatavu.edelphi.domainmodel.resources.Query;
import fi.metatavu.edelphi.query.QueryOption;
import fi.metatavu.edelphi.query.RequiredQueryFragment;
import fi.metatavu.edelphi.smvcj.controllers.PageRequestContext;
import fi.metatavu.edelphi.smvcj.controllers.RequestContext;
import fi.metatavu.edelphi.utils.QueryPageUtils;
import fi.metatavu.edelphi.utils.QueryUtils;
import fi.metatavu.edelphi.utils.SystemUtils;

public abstract class AbstractScaleThesisQueryPageHandler extends AbstractThesisQueryPageHandler {

  protected static final String MULTISELECT_OPTION_NAMEPREFIX = "ms";
  
  public static final int SCALE_TYPE_RADIO = 0;
  public static final int SCALE_TYPE_SLIDER = 1;
  public static final int SCALE_TYPE_GRAPH = 2;
  
  protected void renderRadioList(PageRequestContext requestContext, String name, String label, QueryOptionField queryField, QueryQuestionOptionAnswer answer) {
    List<QueryOptionFieldOption> options = QueryUtils.listQueryOptionFieldOptions(queryField);
    
    RequiredQueryFragment requiredFragment = new RequiredQueryFragment("scale_radiolist");
    requiredFragment.addAttribute("optionsCount", String.valueOf(options.size()));
    
    int i = 0;
    for (QueryOptionFieldOption option : options) {
      requiredFragment.addAttribute(String.format("option.%d.value", i), option.getValue());
      requiredFragment.addAttribute(String.format("option.%d.text", i), option.getText());
      
      if ((answer != null) && option.getValue().equals(answer.getOption().getValue())) {
        requiredFragment.addAttribute(String.format("option.%d.selected", i), "1");
      }

      i++;
    }
    
    requiredFragment.addAttribute("name", name);
    requiredFragment.addAttribute("label", label);
    addRequiredFragment(requestContext, requiredFragment);
  }
  
  protected void renderSlider(PageRequestContext requestContext, String name, String label, QueryOptionField queryField, QueryQuestionOptionAnswer answer) {
    List<QueryOptionFieldOption> options = QueryUtils.listQueryOptionFieldOptions(queryField);
        
    RequiredQueryFragment requiredFragment = new RequiredQueryFragment("scale_slider");
    requiredFragment.addAttribute("optionsCount", String.valueOf(options.size()));
    
    int i = 0;
    for (QueryOptionFieldOption option : options) {
      requiredFragment.addAttribute(String.format("option.%d.value", i), option.getValue());
      requiredFragment.addAttribute(String.format("option.%d.text", i), option.getText());
      
      if ((answer != null) && option.getValue().equals(answer.getOption().getValue())) {
        requiredFragment.addAttribute(String.format("option.%d.selected", i), "1");
      }
    
      i++;
    }
    
    requiredFragment.addAttribute("name", name);
    requiredFragment.addAttribute("label", label);
    addRequiredFragment(requestContext, requiredFragment);
  }
  
  protected void renderMultiselectList(PageRequestContext requestContext, List<QueryOptionFieldOption> options, QueryQuestionMultiOptionAnswer answer) {
    RequiredQueryFragment requiredFragment = new RequiredQueryFragment("multiselect");
    requiredFragment.addAttribute("optionsCount", String.valueOf(options.size()));
    
    int i = 0;

    for (QueryOptionFieldOption option : options) {
      String optionValue = option.getValue();
      String optionText = option.getText();
      String optionName = MULTISELECT_OPTION_NAMEPREFIX + "." + optionValue;

      requiredFragment.addAttribute("option." + i + ".name", optionName);
      requiredFragment.addAttribute("option." + i + ".value", optionValue);
      requiredFragment.addAttribute("option." + i + ".text", optionText);
      
      if (answer != null) {
        if (answer.getOptions().contains(option)) {
          requiredFragment.addAttribute("option." + i + ".selected", "1");
        }
      }

      i++;
    }
    
    addRequiredFragment(requestContext, requiredFragment);
  }  
  
  /**
   * Updates field caption if field can be found. Used only when query already contains replies.
   * 
   * @param queryPage query page
   * @param fieldName field name
   * @param fieldCaption field caption
   */
  protected void synchronizeFieldCaption(QueryPage queryPage, String fieldName, String fieldCaption) {
    QueryFieldDAO queryFieldDAO = new QueryFieldDAO();

    QueryOptionField queryField = (QueryOptionField) queryFieldDAO.findByQueryPageAndName(queryPage, fieldName);
    if (queryField != null)
      queryFieldDAO.updateCaption(queryField, StringUtils.abbreviate(fieldCaption, SystemUtils.MAX_QUERY_FIELD_CAPTION));
  }
  
  /**
   * Synchronizes field meta. Should not be used when field already contains replies
   * 
   * @param settings settings map
   * @param queryPage query page
   * @param optionsOption page option
   * @param fieldName field name
   * @param fieldCaption field caption 
   * @param mandatory whether field is mandatory
   */
  protected void synchronizeField(Map<String, String> settings, QueryPage queryPage, QueryOption optionsOption, String fieldName, String fieldCaption, Boolean mandatory) {
    List<String> options = QueryPageUtils.parseSerializedList(settings.get(optionsOption.getName()));
    synchronizeField(queryPage, options, fieldName, fieldCaption, mandatory);
  }
  
  /**
   * Synchronizes field meta. Should not be used when field already contains replies
   * 
   * @param queryPage query page
   * @param options field  options
   * @param fieldName field name
   * @param fieldCaption field caption 
   * @param mandatory whether field is mandatory
   */
  protected void synchronizeField(QueryPage queryPage, List<String> options, String fieldName, String fieldCaption, Boolean mandatory) {
    QueryFieldDAO queryFieldDAO = new QueryFieldDAO();
    QueryOptionFieldDAO queryOptionFieldDAO = new QueryOptionFieldDAO();

    QueryOptionField queryField = (QueryOptionField) queryFieldDAO.findByQueryPageAndName(queryPage, fieldName);
    if (queryField != null) {
      queryFieldDAO.updateMandatory(queryField, mandatory);
      queryFieldDAO.updateCaption(queryField, StringUtils.abbreviate(fieldCaption, SystemUtils.MAX_QUERY_FIELD_CAPTION));
    } else {
      queryField = queryOptionFieldDAO.create(queryPage, fieldName, mandatory, StringUtils.abbreviate(fieldCaption, SystemUtils.MAX_QUERY_FIELD_CAPTION));
    }

    synchronizeFieldOptions(options, queryField);
  }
  
  private void synchronizeFieldOptions(List<String> options, QueryOptionField queryField) {
    QueryOptionFieldOptionDAO queryOptionFieldOptionDAO = new QueryOptionFieldOptionDAO();
    QueryQuestionOptionAnswerDAO queryQuestionOptionAnswerDAO = new QueryQuestionOptionAnswerDAO();
    
    List<String> oldOptionValues = new ArrayList<>();
    List<QueryOptionFieldOption> oldOptions = queryOptionFieldOptionDAO.listByQueryField(queryField);
    for (QueryOptionFieldOption oldOption : oldOptions) {
      oldOptionValues.add(oldOption.getValue());
    }
    
    int i = 0;
    for (String option : options) {
      String optionValue = String.valueOf(i);
      
      QueryOptionFieldOption optionFieldOption = queryOptionFieldOptionDAO.findByQueryFieldAndValue(queryField, optionValue);
      if (optionFieldOption == null) {
        queryOptionFieldOptionDAO.create(queryField, StringUtils.abbreviate(option, SystemUtils.MAX_QUERY_FIELD_CAPTION), optionValue);
      } else {
        queryOptionFieldOptionDAO.updateText(optionFieldOption, StringUtils.abbreviate(option, SystemUtils.MAX_QUERY_FIELD_CAPTION));
      }
          
      oldOptionValues.remove(optionValue);
      
      i++;
    }
    
    for (String optionValue : oldOptionValues) {
      QueryOptionFieldOption optionFieldOption = queryOptionFieldOptionDAO.findByQueryFieldAndValue(queryField, optionValue);
      if (optionFieldOption != null) {
        long answerCount = queryQuestionOptionAnswerDAO.countByQueryOptionFieldOption(optionFieldOption);
        if (answerCount == 0) {
          queryOptionFieldOptionDAO.delete(optionFieldOption);
        }
        else {
          queryOptionFieldOptionDAO.archive(optionFieldOption);
        }
      }
    }
  }
  
  protected void saveAnswer(RequestContext requestContext, QueryPage queryPage, QueryReply queryReply, String fieldName, String value) {
    QueryFieldDAO queryFieldDAO = new QueryFieldDAO();
    QueryOptionFieldOptionDAO queryOptionFieldOptionDAO = new QueryOptionFieldOptionDAO();
    QueryQuestionOptionAnswerDAO queryQuestionOptionAnswerDAO = new QueryQuestionOptionAnswerDAO();
    
    QueryOptionField queryField = (QueryOptionField) queryFieldDAO.findByQueryPageAndName(queryPage, fieldName);
    Query query = queryPage.getQuerySection().getQuery();
    
    QueryOptionFieldOption fieldOption = queryOptionFieldOptionDAO.findByQueryFieldAndValue(queryField, value);
    if (fieldOption != null) {
      QueryQuestionOptionAnswer answer = queryQuestionOptionAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
      if (answer != null) {
        if (query.getAllowEditReply()) {
          answer = queryQuestionOptionAnswerDAO.updateOption(answer, fieldOption);
        } else {
          throw new IllegalStateException("Could not save reply: Already replied");
        }
      } else {
        answer = queryQuestionOptionAnswerDAO.create(queryReply, queryField, fieldOption);
      }
    } else {
      // "Saving" empty option is legal
//      throw new IllegalArgumentException("Field option '" + value + "' not found");
    }
  }

}
