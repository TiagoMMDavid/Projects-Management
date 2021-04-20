package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes
import pt.isel.daw.g08.project.controllers.models.IssueCreateInputModel
import pt.isel.daw.g08.project.controllers.models.IssueEditInputModel
import pt.isel.daw.g08.project.controllers.models.IssueOutputModel
import pt.isel.daw.g08.project.controllers.models.IssuesOutputModel
import pt.isel.daw.g08.project.database.helpers.IssuesDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/issues")
class IssuesController(val db: IssuesDb) : Routes() {

    @GetMapping
    fun getAllIssues(
        @PathVariable projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val issuesDao = db.getAllIssuesFromProject(pagination.page, pagination.limit, projectId)
        val collectionSize = db.getIssuesCount(projectId)
        val issues = IssuesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = issuesDao.size,
        )
        val issuesUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues"

        return createResponseEntity(
            issues.toSirenObject(
                entities = issuesDao.map {
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
                            SirenLink(listOf("self"), URI("${issuesUri}/${it.iid}")),
                            SirenLink(listOf("state"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.state_id}")),
                            SirenLink(listOf("comments"), URI("${issuesUri}/${it.iid}/comments")),
                            SirenLink(listOf("labels"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${it.iid}/labels")),
                            SirenLink(listOf("author"), URI("${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}")),
                            SirenLink(listOf("project"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}")),
                            SirenLink(listOf("issues"), URI(issuesUri)),
                        )
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-issue",
                        title = "Create Issue",
                        method = HttpMethod.PUT,
                        href = URI(issuesUri),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "name", type = SirenFieldType.text),
                            SirenActionField(name = "description", type = SirenFieldType.text)
                        )
                    )
                ),
                links = createUriListForPagination(issuesUri, pagination.page, issues.pageSize, pagination.limit, issues.collectionSize)
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("{issueId}")
    fun getIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val issueDao = db.getIssueById(issueId)
        val issue = IssueOutputModel(
            id = issueDao.iid,
            name = issueDao.name,
            description = issueDao.description,
            createDate = issueDao.create_date,
            closeDate = issueDao.close_date,
            state = issueDao.state_name,
            project = issueDao.project_name,
            author = issueDao.author_name,
        )

        val selfUri = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issue.id}")

        return createResponseEntity(
            issue.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-issue",
                        title = "Edit Issue",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issue.id),
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
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issue.id),
                        )
                    )
                ),
                links = listOf(
                    SirenLink(listOf("self"), selfUri),
                    SirenLink(listOf("state"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${issueDao.state_id}")),
                    SirenLink(listOf("comments"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issue.id}/comments")),
                    SirenLink(listOf("labels"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issue.id}/labels")),
                    SirenLink(listOf("author"), URI("${env.getBaseUrl()}/${USERS_HREF}/${issueDao.author_id}")),
                    SirenLink(listOf("project"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}")),
                    SirenLink(listOf("issues"), URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues")),
                )
            ),
            HttpStatus.OK
        )
    }

    @PostMapping
    fun createIssue(
        @PathVariable projectId: Int,
        @RequestBody input: IssueCreateInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @PutMapping("{issueId}")
    fun editIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @RequestBody input: IssueEditInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }
}