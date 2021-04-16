package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.IssueDao

private const val GET_ALL_ISSUES_QUERY = "SELECT iid, name, description, create_date, close_date, state_id, state_name, project_id, project_name, author_id, author_name FROM V_ISSUE"

private const val GET_ISSUES_FROM_PROJECT_QUERY = "$GET_ALL_ISSUES_QUERY WHERE project_id = :pid"
private const val GET_ISSUES_COUNT = "SELECT COUNT(iid) as count FROM ISSUE WHERE project = :pid"
private const val GET_ISSUE_QUERY = "$GET_ALL_ISSUES_QUERY WHERE iid = :iid"

@Component
class IssuesDb : DatabaseHelper() {
    fun getAllIssuesFromProject(page: Int, perPage: Int, projectId: Int) =
        boundedGetList(page, perPage, GET_ISSUES_FROM_PROJECT_QUERY, "pid", projectId, IssueDao::class.java)

    fun getIssuesCount(projectId: Int) = boundedGetOne("pid", projectId, GET_ISSUES_COUNT, Int::class.java)
    fun getIssueById(issueId: Int) = boundedGetOne("iid", issueId, GET_ISSUE_QUERY, IssueDao::class.java)
}