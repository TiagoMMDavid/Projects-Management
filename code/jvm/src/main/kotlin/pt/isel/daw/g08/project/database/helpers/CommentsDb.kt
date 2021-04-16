package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.CommentDao

private const val GET_ALL_COMMENTS_QUERY = "SELECT cid, text, create_date, issue_id, issue_name, author_id, author_name FROM V_COMMENT"

private const val GET_COMMENTS_FROM_ISSUE_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE issue_id = :iid"
private const val GET_COMMENTS_COUNT = "SELECT COUNT(cid) as count FROM COMMENT WHERE iid = :iid"
private const val GET_COMMENT_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE cid = :cid"

@Component
class CommentsDb : DatabaseHelper() {
    fun getAllCommentsFromIssue(page: Int, perPage: Int, issueId: Int) =
        boundedGetList(page, perPage, GET_COMMENTS_FROM_ISSUE_QUERY, "iid", issueId, CommentDao::class.java)

    fun getCommentsCount(issueId: Int) = boundedGetOne("iid", issueId, GET_COMMENTS_COUNT, Int::class.java)
    fun getCommentById(commentId: Int) = boundedGetOne("cid", commentId, GET_COMMENT_QUERY, CommentDao::class.java)
}