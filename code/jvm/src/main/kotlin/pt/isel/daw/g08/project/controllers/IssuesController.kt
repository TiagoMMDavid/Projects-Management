package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.ISSUES_HREF
import pt.isel.daw.g08.project.Routes.ISSUE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getCommentsUri
import pt.isel.daw.g08.project.Routes.getIssueByNumberUri
import pt.isel.daw.g08.project.Routes.getIssuesUri
import pt.isel.daw.g08.project.Routes.getLabelsOfIssueUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getStateByNumberUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.controllers.models.IssueCreateInputModel
import pt.isel.daw.g08.project.controllers.models.IssueEditInputModel
import pt.isel.daw.g08.project.controllers.models.IssueOutputModel
import pt.isel.daw.g08.project.controllers.models.IssuesOutputModel
import pt.isel.daw.g08.project.database.helpers.IssuesDb
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
        @PathVariable projectId: Int,
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
                        SirenLink(listOf("self"), getIssueByNumberUri(projectId, it.iid).includeHost()),
                        SirenLink(listOf("state"), getStateByNumberUri(projectId, it.state_id).includeHost()),
                        SirenLink(listOf("comments"), getCommentsUri(projectId, it.iid).includeHost()),
                        SirenLink(listOf("labels"), getLabelsOfIssueUri(projectId, it.iid).includeHost()),
                        SirenLink(listOf("author"), getUserByIdUri(it.author_id).includeHost()),
                        SirenLink(listOf("project"), getProjectByIdUri(it.project_id).includeHost()),
                        SirenLink(listOf("issues"), getIssuesUri(it.project_id).includeHost()),
                    )
                )
            },
            actions = listOf(
                SirenAction(
                    name = "create-issue",
                    title = "Create Issue",
                    method = HttpMethod.PUT,
                    href = getIssuesUri(projectId).includeHost(),
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "name", type = SirenFieldType.text),
                        SirenActionField(name = "description", type = SirenFieldType.text)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                getIssuesUri(projectId).includeHost(),
                pagination.page,
                issuesModel.pageSize,
                pagination.limit,
                issuesModel.collectionSize
            ) + listOf(SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()))
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(ISSUE_BY_NUMBER_HREF)
    fun getIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
    ): ResponseEntity<Response> {
        val issue = db.getIssueByNumber(issueId)
        val issueModel = IssueOutputModel(
            id = issue.iid,
            name = issue.name,
            description = issue.description,
            createDate = issue.create_date,
            closeDate = issue.close_date,
            state = issue.state_name,
            project = issue.project_name,
            author = issue.author_name,
        )

        val selfUri = getIssueByNumberUri(projectId, issue.iid).includeHost()

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
                        SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueModel.id),
                        SirenActionField(name = "name", type = SirenFieldType.text),
                        SirenActionField(name = "description", type = SirenFieldType.text),
                        SirenActionField(name = "stateId", type = SirenFieldType.number),
                    )
                ),
                SirenAction(
                    name = "delete-issue",
                    title = "Delete Issue",
                    method = HttpMethod.DELETE,
                    href = selfUri,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueModel.id),
                    )
                )
            ),
            links = listOf(
                SirenLink(listOf("self"), selfUri),
                SirenLink(listOf("state"), getStateByNumberUri(projectId, issue.state_id).includeHost()),
                SirenLink(listOf("comments"), getCommentsUri(projectId, issue.iid).includeHost()),
                SirenLink(listOf("labels"), getLabelsOfIssueUri(projectId, issue.iid).includeHost()),
                SirenLink(listOf("author"), getUserByIdUri(issue.author_id).includeHost()),
                SirenLink(listOf("project"), getProjectByIdUri(issue.project_id).includeHost()),
                SirenLink(listOf("issues"), getIssuesUri(projectId).includeHost()),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PostMapping(ISSUES_HREF)
    fun createIssue(
        @PathVariable projectId: Int,
        @RequestBody input: IssueCreateInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @RequiresAuth
    @PutMapping(ISSUE_BY_NUMBER_HREF)
    fun editIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: IssueEditInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }
}