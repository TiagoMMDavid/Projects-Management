package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.CommentCreateInputModel
import pt.isel.daw.g08.project.controllers.models.CommentEditInputModel
import pt.isel.daw.g08.project.controllers.models.CommentOutputModel
import pt.isel.daw.g08.project.controllers.models.CommentsOutputModel
import pt.isel.daw.g08.project.database.helpers.CommentsDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/issues/{issueId}/comments")
class CommentsController(val db: CommentsDb) : BaseController() {

    @GetMapping
    fun getIssueComments(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val comments = db.getAllCommentsFromIssue(page, count, issueId)
        val collectionSize = db.getCommentsCount(issueId)

        return createResponseEntity(
            CommentsOutputModel(
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = comments.size,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments{?pageIndex,pageSize}",
                comments = comments.map {
                    CommentOutputModel(
                        id = it.cid,
                        text = it.text,
                        createDate = it.create_date,
                        issueName = it.issue_name,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/${it.cid}",
                        issueUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        commentsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/${it.cid}/comments",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @GetMapping("{commentId}")
    fun getComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable commentId: Int
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val comment = db.getCommentById(commentId)

        return createResponseEntity(
            CommentOutputModel(
                id = comment.cid,
                text = comment.text,
                createDate = comment.create_date,
                issueName = comment.issue_name,
                authorName = comment.author_name,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/${comment.cid}",
                issueUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}",
                authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${comment.author_id}",
                commentsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/${comment.cid}/comments",
            ),
            200
        )
    }

    @PostMapping
    fun addComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: CommentCreateInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @PutMapping("{commentId}")
    fun editComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: CommentEditInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping("{commentId}")
    fun deleteComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable commentId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }
}