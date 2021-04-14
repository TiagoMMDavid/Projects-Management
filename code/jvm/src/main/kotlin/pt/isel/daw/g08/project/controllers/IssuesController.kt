package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*

private const val GET_ALL_ISSUES_QUERY = "SELECT iid, state, project, name, description, create_date, close_date FROM ISSUE WHERE project = :projectName"
private const val GET_ISSUE_QUERY = "SELECT iid, state, project, name, description, create_date, close_date FROM ISSUE WHERE iid = :iid"

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectName}/issues")
class IssuesController : BaseController() {

    @GetMapping
    fun getAllIssues(
        @PathVariable projectName: String,
    ): IssuesOutputModel {
        /*
        val issues = jdbi.withHandle<List<IssueDao>, Exception> {
            it.createQuery(GET_ALL_ISSUES_QUERY)
                .bind("projectName", projectName.urlDecode())
                .mapTo(IssueDao::class.java)
                .list()
        }

        return IssuesOutputModel(issues.map {
            IssueOutputModel(
                id = it.iid,
                name = it.name,
                description = it.description,
                project = it.project,
                state = it.state,
                creationDate = it.createDate,
                closeDate = it.closeDate,
            )
        })
         */
        TODO()
    }

    @GetMapping("{issueId}")
    fun getIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
    ): IssueOutputModel {
        /*
        val issue = jdbi.withHandle<IssueDao, Exception> {
            it.createQuery(GET_ISSUE_QUERY)
                .bind("iid", issueId)
                .mapTo(IssueDao::class.java)
                .one()
        }

        return IssueOutputModel(
            id = issue.iid,
            name = issue.name,
            description = issue.description,
            project = issue.project,
            state = issue.state,
            creationDate = issue.createDate,
            closeDate = issue.closeDate,
        )
        */
        TODO()
    }

    @PostMapping
    fun createIssue(
        @PathVariable projectName: String,
        @RequestBody input: IssueCreateInputModel,
    ): IssueCreateResponseModel {
        TODO()
    }

    @PutMapping("{issueId}")
    fun editIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: IssueEditInputModel,
    ): IssueEditResponseModel {
        TODO()
    }
}