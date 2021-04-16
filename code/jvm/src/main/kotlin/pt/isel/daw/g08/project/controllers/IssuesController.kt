package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.IssueCreateInputModel
import pt.isel.daw.g08.project.controllers.models.IssueEditInputModel
import pt.isel.daw.g08.project.controllers.models.IssueOutputModel
import pt.isel.daw.g08.project.controllers.models.IssuesOutputModel
import pt.isel.daw.g08.project.database.helpers.IssuesDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/issues")
class IssuesController(val db: IssuesDb) : BaseController() {

    @GetMapping
    fun getAllIssues(
        @PathVariable projectId: Int,
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val issues = db.getAllIssuesFromProject(page, count, projectId)
        val collectionSize = db.getIssuesCount(projectId)

        return createResponseEntity(
            IssuesOutputModel(
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = issues.size,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues{?pageIndex,pageSize}",
                issues = issues.map {
                    IssueOutputModel(
                        id = it.iid,
                        name = it.name,
                        description = it.description,
                        createDate = it.create_date,
                        closeDate = it.close_date,
                        stateName = it.state_name,
                        projectName = it.project_name,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${it.iid}",
                        stateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.state_id}",
                        commentsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${it.iid}/comments",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                        issuesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @GetMapping("{issueId}")
    fun getIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val issue = db.getIssueById(issueId)

        return createResponseEntity(
            IssueOutputModel(
                id = issue.iid,
                name = issue.name,
                description = issue.description,
                createDate = issue.create_date,
                closeDate = issue.close_date,
                stateName = issue.state_name,
                projectName = issue.project_name,
                authorName = issue.author_name,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issue.iid}",
                stateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${issue.state_id}",
                commentsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issue.iid}/comments",
                authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${issue.author_id}",
                projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                issuesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues"
            ),
            200
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