package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes
import pt.isel.daw.g08.project.controllers.models.CommentCreateInputModel
import pt.isel.daw.g08.project.controllers.models.CommentEditInputModel
import pt.isel.daw.g08.project.controllers.models.CommentOutputModel
import pt.isel.daw.g08.project.controllers.models.CommentsOutputModel
import pt.isel.daw.g08.project.database.helpers.CommentsDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/issues/{issueId}/comments")
class CommentsController(val db: CommentsDb) : Routes() {


    @GetMapping
    fun getIssueComments(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val commentsDao = db.getAllCommentsFromIssue(pagination.page, pagination.limit, issueId)
        val collectionSize = db.getCommentsCount(issueId)
        val comments = CommentsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = commentsDao.size
        )

        val commentsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments"

        return createResponseEntity(
            comments.toSirenObject(
                entities = commentsDao.map {
                    CommentOutputModel(
                        id = it.cid,
                        content = it.text,
                        createDate = it.create_date,
                        issue = it.issue_name,
                        author = it.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(listOf("self"), URI("${commentsUri}/${it.cid}")),
                            SirenLink(listOf("issue"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}")),
                            SirenLink(listOf("author"), URI("${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}")),
                            SirenLink(listOf("comments"), URI(commentsUri)),
                        )
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-comment",
                        title = "Create Comment",
                        method = HttpMethod.PUT,
                        href = URI(commentsUri),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                            SirenActionField(name = "content", type = SirenFieldType.text)
                        )
                    )
                ),
                links = createUriListForPagination(commentsUri, pagination.page, comments.pageSize, pagination.limit, comments.collectionSize)
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("{commentId}")
    fun getComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable commentId: Int
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val commentDao = db.getCommentById(commentId)
        val comment = CommentOutputModel(
            id = commentDao.cid,
            content = commentDao.text,
            createDate = commentDao.create_date,
            issue = commentDao.issue_name,
            author = commentDao.author_name,
        )

        val selfUri = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/${comment.id}")

        return createResponseEntity(
            comment.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-comment",
                        title = "Edit Comment",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                            SirenActionField(name = "commentId", type = SirenFieldType.hidden, value = comment.id),
                            SirenActionField(name = "content", type = SirenFieldType.text)
                        )
                    ),
                    SirenAction(
                        name = "delete-comment",
                        title = "Delete Comment",
                        method = HttpMethod.DELETE,
                        href = selfUri,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                            SirenActionField(name = "commentId", type = SirenFieldType.hidden, value = comment.id),
                        )
                    )
                ),
                links = listOf(
                    SirenLink(listOf("self"), selfUri),
                    SirenLink(listOf("issue"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}")),
                    SirenLink(listOf("author"), URI("${env.getBaseUrl()}/${USERS_HREF}/${commentDao.author_id}")),
                    SirenLink(listOf("comments"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments")),
                )
            ),
            HttpStatus.OK
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