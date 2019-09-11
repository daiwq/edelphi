/**
 * eDelphi REST API
 * REST API for eDelphi
 *
 * OpenAPI spec version: 1.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


export interface QueryQuestionCommentCategory { 
    /**
     * Comment category's id
     */
    readonly id?: number;
    /**
     * Comment category's name
     */
    name: string;
    /**
     * Query's id where the comment is
     */
    queryId: number;
    /**
     * Query page's id or null if category is query scoped
     */
    queryPageId?: number;
}
export interface QueryQuestionCommentCategoryOpt { 
    /**
     * Comment category's id
     */
    readonly id?: number;
    /**
     * Comment category's name
     */
    name?: string;
    /**
     * Query's id where the comment is
     */
    queryId?: number;
    /**
     * Query page's id or null if category is query scoped
     */
    queryPageId?: number;
}
