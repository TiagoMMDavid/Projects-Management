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
import pt.isel.daw.g08.project.Routes.COMMENT_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.COMMENT_PARAM
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.ISSUE_PARAM
import pt.isel.daw.g08.project.Routes.PROJECT_PARAM
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getCommentByNumberUri
import pt.isel.daw.g08.project.Routes.getCommentsUri
import pt.isel.daw.g08.project.Routes.getIssueByNumberUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.controllers.models.CommentInputModel
import pt.isel.daw.g08.project.controllers.models.CommentOutputModel
import pt.isel.daw.g08.project.controllers.models.CommentsOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.CommentsDb
import pt.isel.daw.g08.project.database.helpers.IssuesDb
import pt.isel.daw.g08.project.exceptions.ForbiddenException
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity

@RestController
class CommentsController(
    val commentsDb: CommentsDb,
    val issuesDb: IssuesDb
) {

    @RequiresAuth
    @GetMapping(COMMENTS_HREF)
    fun getIssueComments(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val issue = issuesDb.getIssueByNumber(projectId, issueNumber)
        val comments = commentsDb.getAllCommentsFromIssue(pagination.page, pagination.limit, projectId, issueNumber)
        val collectionSize = commentsDb.getCommentsCount(projectId, issueNumber)
        val commentsModel = CommentsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = comments.size
        )

        val actions =
            if (issue.state_name == "archived") {
                null
            } else {
                listOf(
                    SirenAction(
                        name = "create-comment",
                        title = "Create Comment",
                        method = HttpMethod.POST,
                        href = getCommentsUri(projectId, issueNumber),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueNumber),
                            SirenActionField(name = "content", type = SirenFieldType.text)
                        )
                    )
                )
            }

        return commentsModel.toSirenObject(
            entities = comments.map {
                CommentOutputModel(
                    id = it.cid,
                    number = it.number,
                    content = it.text,
                    createDate = it.create_date,
                    project = it.project_name,
                    projectId = it.project_id,
                    issue = it.issue_name,
                    issueNumber = it.issue_number,
                    author = it.author_name,
                    authorId = it.author_id
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(listOf("self"), getCommentByNumberUri(projectId, issueNumber, it.number)),
                        SirenLink(listOf("issue"), getIssueByNumberUri(projectId, issueNumber)),
                        SirenLink(listOf("author"), getUserByIdUri(it.author_id)),
                        SirenLink(listOf("comments"), getCommentsUri(projectId, issueNumber)),
                    )
                )
            },
            actions = actions,
            links = createSirenLinkListForPagination(
                getCommentsUri(projectId, issueNumber),
                pagination.page,
                pagination.limit,
                commentsModel.collectionSize
            ) + listOf(
                SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId)),
                SirenLink(rel = listOf("issue"), href = getIssueByNumberUri(projectId, issueNumber)),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(COMMENT_BY_NUMBER_HREF)
    fun getComment(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        @PathVariable(name = COMMENT_PARAM) commentNumber: Int
    ): ResponseEntity<Response> {
        val selfUri = getCommentByNumberUri(projectId, issueNumber, commentNumber)

        val issue = issuesDb.getIssueByNumber(projectId, issueNumber)
        val comment = commentsDb.getCommentByNumber(projectId, issueNumber, commentNumber)

        val actions =
            if (issue.state_name == "archived") {
                null
            } else {
                listOf(
                    SirenAction(
                        name = "edit-comment",
                        title = "Edit Comment",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueNumber),
                            SirenActionField(name = "commentNumber", type = SirenFieldType.hidden, value = comment.number),
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
                            SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueNumber),
                            SirenActionField(name = "commentNumber", type = SirenFieldType.hidden, value = comment.number),
                        )
                    )
                )
            }

        val commentModel = CommentOutputModel(
            id = comment.cid,
            number = comment.number,
            content = comment.text,
            createDate = comment.create_date,
            project = comment.project_name,
            projectId = comment.project_id,
            issue = comment.issue_name,
            issueNumber = comment.issue_number,
            author = comment.author_name,
            authorId = comment.author_id
        )

        return commentModel.toSirenObject(
            actions = actions,
            links = listOf(
                SirenLink(listOf("self"), selfUri),
                SirenLink(listOf("issue"), getIssueByNumberUri(projectId, issueNumber)),
                SirenLink(listOf("author"), getUserByIdUri(comment.author_id)),
                SirenLink(listOf("comments"), getCommentsUri(projectId, issueNumber)),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PostMapping(COMMENTS_HREF)
    fun createComment(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        @RequestBody input: CommentInputModel,
        user: User,
    ): ResponseEntity<Any> {
        if (input.content == null) throw InvalidInputException("Missing content")

        val comment = commentsDb.createComment(projectId, issueNumber, user.uid, input.content)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getCommentByNumberUri(projectId, issueNumber, comment.number).toString())
            .body(null)
    }

    @RequiresAuth
    @PutMapping(COMMENT_BY_NUMBER_HREF)
    fun editComment(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        @PathVariable(name = COMMENT_PARAM) commentNumber: Int,
        @RequestBody input: CommentInputModel,
    ): ResponseEntity<Any> {
        if (input.content == null) throw InvalidInputException("Missing content")

        commentsDb.editComment(projectId, issueNumber, commentNumber, input.content)
        return ResponseEntity
            .status(HttpStatus.OK)
            .header("Location", getCommentByNumberUri(projectId, issueNumber, commentNumber).toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(COMMENT_BY_NUMBER_HREF)
    fun deleteComment(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        @PathVariable(name = COMMENT_PARAM) commentNumber: Int
    ): ResponseEntity<Any> {
        val issue = issuesDb.getIssueByNumber(projectId, issueNumber)
        if (issue.state_name == "archived") throw ForbiddenException("Cannot delete comments from 'archived' issues")

        commentsDb.deleteComment(projectId, issueNumber, commentNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }
}