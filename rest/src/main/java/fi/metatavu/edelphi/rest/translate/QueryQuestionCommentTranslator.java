package fi.metatavu.edelphi.rest.translate;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.edelphi.domainmodel.querydata.QueryQuestionComment;

/**
 * Translator for QueryQuestionComments
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class QueryQuestionCommentTranslator extends AbstractTranslator<QueryQuestionComment, fi.metatavu.edelphi.rest.model.QueryQuestionComment> {

  @Override
  public fi.metatavu.edelphi.rest.model.QueryQuestionComment translate(QueryQuestionComment entity) {
    if (entity == null) {
      return null;
    }
   
    fi.metatavu.edelphi.rest.model.QueryQuestionComment result = new fi.metatavu.edelphi.rest.model.QueryQuestionComment();
    result.setContents(entity.getComment());
    result.setHidden(entity.getHidden());
    result.setId(entity.getId());
    result.setParentId(entity.getParentComment() != null ? entity.getParentComment().getId() : null);
    result.setQueryPageId(entity.getQueryPage() != null ? entity.getQueryPage().getId() : null);
    result.setQueryReplyId(entity.getQueryReply() != null ? entity.getQueryReply().getId() : null);
    result.setLastModified(translateDate(entity.getLastModified()));
    result.setLastModifierId(translateUserId(entity.getLastModifier()));
    result.setCreated(translateDate(entity.getCreated()));
    result.setCreatorId(translateUserId(entity.getCreator()));
    
    return result;
  }

}
