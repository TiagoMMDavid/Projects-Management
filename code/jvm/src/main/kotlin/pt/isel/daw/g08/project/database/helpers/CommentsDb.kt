package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.CommentDao

private const val GET_ALL_COMMENTS_QUERY = "SELECT cid, text, create_date, issue_id, issue_name, author_id, author_name FROM V_COMMENT"

private const val GET_COMMENTS_FROM_ISSUE_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE issue_id = :iid ORDER BY cid"
private const val GET_COMMENTS_COUNT = "SELECT COUNT(cid) as count FROM COMMENT WHERE iid = :iid"
private const val GET_COMMENT_QUERY = "$GET_ALL_COMMENTS_QUERY WHERE cid = :cid"

@Component
class CommentsDb : DatabaseHelper() {
    fun getAllCommentsFromIssue(page: Int, perPage: Int, issueId: Int) =
        getList(GET_COMMENTS_FROM_ISSUE_QUERY, CommentDao::class.java, page, perPage, Pair("iid", issueId))

    fun getCommentsCount(issueId: Int) = getOne(GET_COMMENTS_COUNT, Int::class.java, Pair("iid", issueId))
    fun getCommentById(commentId: Int) = getOne(GET_COMMENT_QUERY, CommentDao::class.java, Pair("cid", commentId))
}