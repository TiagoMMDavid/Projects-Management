package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*

@RestController
@RequestMapping("/api/projects/{projectName}/issues/{issueId}/comments")
class CommentsController(val jdbi: Jdbi) {

    @GetMapping
    fun getIssueComments(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
    ): CommentsOutputModel {
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