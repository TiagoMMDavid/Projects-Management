package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Comment

private const val GET_ALL_COMMENTS_QUERY = "SELECT cid, text, create_date, issue_id, issue_name, author_id, author_name FROM V_COMMENT"

private const val GET_COMMENTS_FROM_ISSUE_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE issue_id = :iid ORDER BY cid"
private const val GET_COMMENTS_COUNT = "SELECT COUNT(cid) as count FROM COMMENT WHERE iid = :iid"
private const val GET_COMMENT_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE cid = :cid"

@Component
class CommentsDb(val jdbi: Jdbi) {
    fun getAllCommentsFromIssue(page: Int, perPage: Int, issueId: Int) =
        jdbi.getList(GET_COMMENTS_FROM_ISSUE_QUERY, Comment::class.java, page, perPage, mapOf("iid" to issueId))

    fun getCommentsCount(issueId: Int) = jdbi.getOne(GET_COMMENTS_COUNT, Int::class.java, mapOf("iid" to issueId))
    fun getCommentById(commentId: Int) = jdbi.getOne(GET_COMMENT_QUERY, Comment::class.java, mapOf("cid" to commentId))
}