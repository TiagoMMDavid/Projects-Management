package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.CommentCreateInputModel
import pt.isel.daw.g08.project.controllers.models.CommentEditInputModel
import pt.isel.daw.g08.project.controllers.models.CommentOutputModel
import pt.isel.daw.g08.project.controllers.models.CommentsOutputModel
import pt.isel.daw.g08.project.database.helpers.CommentsDb
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

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
        val commentsDao = db.getAllCommentsFromIssue(page, count, issueId)
        val collectionSize = db.getCommentsCount(issueId)
        val comments = CommentsOutputModel(
            collectionSize = collectionSize,
            pageIndex = page,
            pageSize = commentsDao.size
        )

        val commentsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/comments/"

        return createResponseEntity(
            comments.toSirenObject(
                entities = commentsDao.map {
                    CommentOutputModel(
                        id = it.cid,
                        text = it.text,
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
                            SirenActionField(name = "text", type = SirenFieldType.text)
                        )
                    )
                ),
                links = createUriListForPagination(commentsUri, page, comments.pageSize, count, comments.collectionSize)
            ),
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
        val commentDao = db.getCommentById(commentId)
        val comment = CommentOutputModel(
            id = commentDao.cid,
            text = commentDao.text,
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
                            SirenActionField(name = "text", type = SirenFieldType.text)
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