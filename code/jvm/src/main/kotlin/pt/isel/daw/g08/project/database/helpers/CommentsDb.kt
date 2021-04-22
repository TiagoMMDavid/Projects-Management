package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Comment

private const val GET_ALL_COMMENTS_QUERY =
    "SELECT cid, number, text, create_date, issue_id, issue_name, issue_number, project_id, author_id, author_name FROM V_COMMENT"

private const val GET_COMMENTS_FROM_ISSUE_QUERY =
    "$GET_ALL_COMMENTS_QUERY WHERE issue_number = :issueNumber AND project_id = :projectId ORDER BY number"
private const val GET_COMMENTS_COUNT =
    "SELECT COUNT(cid) as count FROM V_COMMENT WHERE issue_number = :issueNumber AND project_id = :projectId"
private const val GET_COMMENT_QUERY =
    "$GET_ALL_COMMENTS_QUERY WHERE number = :number AND issue_number = :issueNumber AND project_id = :projectId"
private const val GET_COMMENT_BY_ID_QUERY =
    "$GET_ALL_COMMENTS_QUERY WHERE cid = :cid"

private const val CREATE_COMMENT_QUERY = "INSERT INTO COMMENT(text, iid, author) VALUES(:text, :iid, :author)"
private const val UPDATE_COMMENT_START = "UPDATE COMMENT SET"
private const val UPDATE_COMMENT_END = "WHERE iid = :iid AND number = :number"
private const val DELETE_COMMENT_QUERY = "DELETE FROM COMMENT WHERE iid = :iid AND number = :number"

@Component
class CommentsDb(
    val issuesDb: IssuesDb,
    val jdbi: Jdbi
) {

    fun getAllCommentsFromIssue(page: Int, perPage: Int, projectId: Int, issueNumber: Int) =
        jdbi.getList(GET_COMMENTS_FROM_ISSUE_QUERY, Comment::class.java, page, perPage,
            mapOf(
                "issueNumber" to issueNumber,
                "projectId" to projectId
            )
        )

    fun getCommentsCount(projectId: Int, issueNumber: Int) =
        jdbi.getOne(GET_COMMENTS_COUNT, Int::class.java,
            mapOf(
                "issueNumber" to issueNumber,
                "projectId" to projectId
            )
        )

    fun getCommentByNumber(projectId: Int, issueNumber: Int, commentNumber: Int) =
        jdbi.getOne(GET_COMMENT_QUERY, Comment::class.java,
            mapOf(
                "number" to commentNumber,
                "issueNumber" to issueNumber,
                "projectId" to projectId
            )
        )

    fun createComment(projectId: Int, issueNumber: Int, userId: Int, content: String): Comment {
        val issueId = issuesDb.getIssueByNumber(projectId, issueNumber).iid
        return jdbi.insertAndGet(
            CREATE_COMMENT_QUERY, Int::class.java,
            GET_COMMENT_BY_ID_QUERY, Comment::class.java,
            mapOf(
                "text" to content,
                "iid" to issueId,
                "author" to userId
            ),
            "cid"
        )
    }

    fun editComment(projectId: Int, issueNumber: Int, commentNumber: Int, content: String) {
        val issueId = issuesDb.getIssueByNumber(projectId, issueNumber).iid
        jdbi.update(
            UPDATE_COMMENT_START,
            mapOf("text" to content),
            UPDATE_COMMENT_END,
            mapOf("iid" to issueId, "number" to commentNumber),
        )
    }

    fun deleteComment(projectId: Int, issueNumber: Int, commentNumber: Int) {
        val issueId = issuesDb.getIssueByNumber(projectId, issueNumber).iid
        jdbi.delete(DELETE_COMMENT_QUERY,
            mapOf("iid" to issueId, "number" to commentNumber)
        )
    }
}