package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*

private const val GET_COMMENTS_QUERY = "SELECT cid, iid, text, create_date FROM COMMENT WHERE iid = :iid"

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectName}/issues/{issueId}/comments")
class CommentsController : BaseController() {

    @GetMapping
    fun getIssueComments(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
    ): CommentsOutputModel {
        /*
        val comments = jdbi.withHandle<List<CommentDao>, Exception> {
            it.createQuery(GET_COMMENTS_QUERY)
                .bind("iid", issueId)
                .mapTo(CommentDao::class.java)
                .list()
        }

        return CommentsOutputModel(comments.map {
            CommentOutputModel(
                comment = it.text,
                createdAt = it.createDate,
            )
        })
         */
        TODO()
    }

    @PostMapping
    fun addComment(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: CommentCreateInputModel,
    ): CommentCreateOutputModel {
        TODO()
    }

    @PutMapping("{commentId}")
    fun editComment(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: CommentEditInputModel,
    ): CommentEditOutputModel {
        TODO()
    }

    @DeleteMapping("{commentId}")
    fun deleteComment(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @PathVariable commendId: Int,
    ): CommentDeleteOutputModel {
        TODO()
    }
}