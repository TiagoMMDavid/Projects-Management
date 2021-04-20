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
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.COMMENTS_HREF
import pt.isel.daw.g08.project.Routes.COMMENT_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.PROJECTS_HREF
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getCommentByIdUri
import pt.isel.daw.g08.project.Routes.getIssueByIdUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
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
import pt.isel.daw.g08.project.responses.toResponseEntity
import java.net.URI

@RestController
class CommentsController(val db: CommentsDb) {

    @GetMapping(COMMENTS_HREF)
    fun getIssueComments(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val comments = db.getAllCommentsFromIssue(pagination.page, pagination.limit, issueId)
        val collectionSize = db.getCommentsCount(issueId)
        val commentsModel = CommentsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = comments.size
        )

        return commentsModel.toSirenObject(
            entities = comments.map {
                CommentOutputModel(
                    id = it.cid,
                    content = it.text,
                    createDate = it.create_date,
                    issue = it.issue_name,
                    author = it.author_name,
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(listOf("self"), getCommentByIdUri(projectId, issueId, it.cid).includeHost()),
                        SirenLink(listOf("issue"), getIssueByIdUri(projectId, issueId).includeHost()),
                        SirenLink(listOf("author"), getUserByIdUri(it.author_id).includeHost()),
                        SirenLink(listOf("comments"), URI(COMMENTS_HREF).includeHost()),
                    )
                )
            },
            actions = listOf(
                SirenAction(
                    name = "create-comment",
                    title = "Create Comment",
                    method = HttpMethod.PUT,
                    href = URI(COMMENTS_HREF).includeHost(),
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                        SirenActionField(name = "content", type = SirenFieldType.text)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                URI(COMMENTS_HREF).includeHost(),
                pagination.page,
                commentsModel.pageSize,
                pagination.limit,
                commentsModel.collectionSize
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @GetMapping(COMMENT_BY_ID_HREF)
    fun getComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable commentId: Int
    ): ResponseEntity<Response> {
        val comment = db.getCommentById(commentId)
        val commentModel = CommentOutputModel(
            id = comment.cid,
            content = comment.text,
            createDate = comment.create_date,
            issue = comment.issue_name,
            author = comment.author_name,
        )

        val selfUri = getCommentByIdUri(projectId, issueId, commentId).includeHost()

        return commentModel.toSirenObject(
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
                        SirenActionField(name = "commentId", type = SirenFieldType.hidden, value = commentModel.id),
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
                        SirenActionField(name = "commentId", type = SirenFieldType.hidden, value = commentModel.id),
                    )
                )
            ),
            links = listOf(
                SirenLink(listOf("self"), selfUri),
                SirenLink(listOf("issue"), getIssueByIdUri(projectId, issueId).includeHost()),
                SirenLink(listOf("author"), getUserByIdUri(comment.author_id).includeHost()),
                SirenLink(listOf("comments"), URI(COMMENTS_HREF).includeHost()),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @PostMapping(COMMENTS_HREF)
    fun addComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: CommentCreateInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @PutMapping(COMMENT_BY_ID_HREF)
    fun editComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: CommentEditInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping(COMMENT_BY_ID_HREF)
    fun deleteComment(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable commentId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }
}