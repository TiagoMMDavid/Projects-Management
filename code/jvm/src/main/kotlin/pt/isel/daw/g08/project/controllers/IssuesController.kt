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
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.ISSUES_HREF
import pt.isel.daw.g08.project.Routes.ISSUE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.ISSUE_PARAM
import pt.isel.daw.g08.project.Routes.PROJECT_PARAM
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getCommentsUri
import pt.isel.daw.g08.project.Routes.getIssueByNumberUri
import pt.isel.daw.g08.project.Routes.getIssuesUri
import pt.isel.daw.g08.project.Routes.getLabelsOfIssueUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getStateByNumberUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.controllers.models.IssueCreateInputModel
import pt.isel.daw.g08.project.controllers.models.IssueEditInputModel
import pt.isel.daw.g08.project.controllers.models.IssueOutputModel
import pt.isel.daw.g08.project.controllers.models.IssuesOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.IssuesDb
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
class IssuesController(val db: IssuesDb) {

    @RequiresAuth
    @GetMapping(ISSUES_HREF)
    fun getAllIssues(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val issues = db.getAllIssuesFromProject(pagination.page, pagination.limit, projectId)
        val collectionSize = db.getIssuesCount(projectId)
        val issuesModel = IssuesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = issues.size,
        )

        return issuesModel.toSirenObject(
            entities = issues.map {
                IssueOutputModel(
                    id = it.iid,
                    number = it.number,
                    name = it.name,
                    description = it.description,
                    createDate = it.create_date,
                    closeDate = it.close_date,
                    state = it.state_name,
                    project = it.project_name,
                    author = it.author_name,
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(listOf("self"), getIssueByNumberUri(projectId, it.number)),
                        SirenLink(listOf("state"), getStateByNumberUri(projectId, it.state_number)),
                        SirenLink(listOf("comments"), getCommentsUri(projectId, it.number)),
                        SirenLink(listOf("labels"), getLabelsOfIssueUri(projectId, it.number)),
                        SirenLink(listOf("author"), getUserByIdUri(it.author_id)),
                        SirenLink(listOf("project"), getProjectByIdUri(it.project_id)),
                        SirenLink(listOf("issues"), getIssuesUri(it.project_id)),
                    )
                )
            },
            actions = listOf(
                SirenAction(
                    name = "create-issue",
                    title = "Create Issue",
                    method = HttpMethod.PUT,
                    href = getIssuesUri(projectId),
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "name", type = SirenFieldType.text),
                        SirenActionField(name = "description", type = SirenFieldType.text)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                getIssuesUri(projectId),
                pagination.page,
                pagination.limit,
                issuesModel.collectionSize
            ) + listOf(SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId)))
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(ISSUE_BY_NUMBER_HREF)
    fun getIssue(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        @PathVariable(ISSUE_PARAM) issueNumber: Int,
    ): ResponseEntity<Response> {
        val issue = db.getIssueByNumber(projectId, issueNumber)
        val issueModel = IssueOutputModel(
            id = issue.iid,
            number = issue.number,
            name = issue.name,
            description = issue.description,
            createDate = issue.create_date,
            closeDate = issue.close_date,
            state = issue.state_name,
            project = issue.project_name,
            author = issue.author_name,
        )

        val selfUri = getIssueByNumberUri(projectId, issue.number)

        return issueModel.toSirenObject(
            actions = listOf(
                SirenAction(
                    name = "edit-issue",
                    title = "Edit Issue",
                    method = HttpMethod.PUT,
                    href = selfUri,
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueModel.number),
                        SirenActionField(name = "name", type = SirenFieldType.text),
                        SirenActionField(name = "description", type = SirenFieldType.text),
                        SirenActionField(name = "state", type = SirenFieldType.text),
                    )
                ),
                SirenAction(
                    name = "delete-issue",
                    title = "Delete Issue",
                    method = HttpMethod.DELETE,
                    href = selfUri,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueModel.number),
                    )
                )
            ),
            links = listOf(
                SirenLink(listOf("self"), selfUri),
                SirenLink(listOf("state"), getStateByNumberUri(projectId, issue.state_number)),
                SirenLink(listOf("comments"), getCommentsUri(projectId, issue.number)),
                SirenLink(listOf("labels"), getLabelsOfIssueUri(projectId, issue.number)),
                SirenLink(listOf("author"), getUserByIdUri(issue.author_id)),
                SirenLink(listOf("project"), getProjectByIdUri(issue.project_id)),
                SirenLink(listOf("issues"), getIssuesUri(projectId)),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PostMapping(ISSUES_HREF)
    fun createIssue(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        @RequestBody input: IssueCreateInputModel,
        user: User
    ): ResponseEntity<Response> {
        if (input.name == null) throw InvalidInputException("Missing name")
        if (input.description == null) throw InvalidInputException("Missing description")

        val issue = db.createIssue(projectId, input.name, input.description, user.uid)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getIssueByNumberUri(projectId, issue.number).toString())
            .body(null)
    }

    @RequiresAuth
    @PutMapping(ISSUE_BY_NUMBER_HREF)
    fun editIssue(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        @PathVariable(ISSUE_PARAM) issueNumber: Int,
        @RequestBody input: IssueEditInputModel,
    ): ResponseEntity<Response> {
        if (input.name == null && input.description == null && input.state == null)
            throw InvalidInputException("Missing name, description or state")

        db.editIssue(projectId, issueNumber, input.name, input.description, input.state)

        return ResponseEntity
            .status(HttpStatus.OK)
            .header("Location", getIssueByNumberUri(projectId, issueNumber).toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(ISSUE_BY_NUMBER_HREF)
    fun deleteIssue(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        @PathVariable(ISSUE_PARAM) issueNumber: Int,
    ): ResponseEntity<Response> {
        db.deleteIssue(projectId, issueNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }
}