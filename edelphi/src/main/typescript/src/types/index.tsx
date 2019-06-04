
/**
 * Redux store state
 */
export interface StoreState {
  accessToken?: AccessToken,
  locale: string,
  queryValidationMessage: string | null
}

export interface PageChangeEvent {
  
}

/**
 * Interface describing an access token
 */
export interface AccessToken {
  token: string
  expires: Date,
  userId: string
}

/**
 * Interface describing an query question comment notification MQTT message
 */
export interface QueryQuestionCommentNotification {
  type: "CREATED" | "UPDATED" | "DELETED";
  panelId: number,
  queryId: number,
  pageId: number,
  commentId: number,
  parentCommentId: number | null,
}

/**
 * Interface describing an query question comment notification MQTT message
 */
export interface QueryQuestionAnswerNotification {
  type: "UPDATED";
  panelId: number,
  queryId: number,
  pageId: number,
  answerId: string,
}

/**
 * Command
 */
export type Command = "edit-page-comment-options" | "edit-page-live2d-options" | "enable-query-next" | "disable-query-next";

/**
 * Command data for save query answers event
 */
export interface SaveQueryAnswersCommandData {
  pageType: string,
  currentPageNumber: number,
  nextPageNumber: number,
  previousPageNumber: number
}

export interface DisableQueryNextCommandEventData {
  reason?: string
} 

export interface EnableQueryNextCommandEventData {
  
} 

/**
 * Command detail for enable query event
 */
export interface DisableQueryNextCommandEventDetail {
  command: "disable-query-next",
  data: DisableQueryNextCommandEventData
}

/**
 * Command detail for enable query event
 */
export interface EnableQueryNextCommandEventDetail {
  command: "enable-query-next",
  data: EnableQueryNextCommandEventData
}

/**
 * Command event for save query answers event
 */
export interface CommandEvent extends CustomEvent {
  detail: DisableQueryNextCommandEventDetail |EnableQueryNextCommandEventDetail
} 

/**
 * Command page data for legacy command
 */
export interface EditPageLegacyCommandPageData {
  hasAnswers: "true" | "false",
  id: number,
  number: number,
  options: {
    caption: string,
    name: string,
    editor: string
  }[],
  title: string,
  type: string
}

/**
 * Command payload for comment options command
 */
export interface EditPageCommentOptionsCommand {
  type: "edit-page-comment-options",
  pageData: EditPageLegacyCommandPageData
}

/**
 * Command payload for live2d options command
 */
export interface EditPageLive2dOptionsCommand {
  type: "edit-page-live2d-options",
  pageData: EditPageLegacyCommandPageData
}