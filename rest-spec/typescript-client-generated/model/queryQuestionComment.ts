/**
 * eDelphi REST API
 * REST API for eDelphi
 *
 * OpenAPI spec version: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


export interface QueryQuestionComment { 
    /**
     * Comment's id
     */
    id?: number;
    /**
     * PArent comment's id
     */
    parentId?: number;
    /**
     * Whether the comment has been hided by the manager
     */
    hidden?: boolean;
    /**
     * Page's id where the comment is
     */
    queryPageId: number;
    /**
     * Comment's query reply id
     */
    queryReplyId: number;
    /**
     * Comment's contents
     */
    contents?: string;
    /**
     * Comment's creator id
     */
    readonly creatorId?: string;
    /**
     * Comment's last modifier id
     */
    readonly lastModifierId?: string;
    /**
     * Comment's creation time
     */
    readonly created?: string;
    /**
     * Comment's last modification time
     */
    readonly lastModified?: string;
}
export interface QueryQuestionCommentOpt { 
    /**
     * Comment's id
     */
    id?: number;
    /**
     * PArent comment's id
     */
    parentId?: number;
    /**
     * Whether the comment has been hided by the manager
     */
    hidden?: boolean;
    /**
     * Page's id where the comment is
     */
    queryPageId?: number;
    /**
     * Comment's query reply id
     */
    queryReplyId?: number;
    /**
     * Comment's contents
     */
    contents?: string;
    /**
     * Comment's creator id
     */
    readonly creatorId?: string;
    /**
     * Comment's last modifier id
     */
    readonly lastModifierId?: string;
    /**
     * Comment's creation time
     */
    readonly created?: string;
    /**
     * Comment's last modification time
     */
    readonly lastModified?: string;
}
