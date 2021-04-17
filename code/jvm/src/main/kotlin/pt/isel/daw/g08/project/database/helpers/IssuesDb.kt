package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.IssueDao

private const val GET_ISSUES_BASE = "SELECT iid, name, description, create_date, close_date, state_id, state_name, project_id, project_name, author_id, author_name FROM V_ISSUE"

private const val GET_ISSUES_FROM_PROJECT_QUERY = "$GET_ISSUES_BASE WHERE project_id = :pid ORDER BY iid"
private const val GET_ISSUES_COUNT = "SELECT COUNT(iid) as count FROM ISSUE WHERE project = :pid"
private const val GET_ISSUE_QUERY = "$GET_ISSUES_BASE WHERE iid = :iid"

@Component
class IssuesDb : DatabaseHelper() {
    fun getAllIssuesFromProject(page: Int, perPage: Int, projectId: Int) =
        getList(GET_ISSUES_FROM_PROJECT_QUERY, IssueDao::class.java, page, perPage, Pair("pid", projectId))

    fun getIssuesCount(projectId: Int) = getOne(GET_ISSUES_COUNT, Int::class.java, Pair("pid", projectId))
    fun getIssueById(issueId: Int) = getOne(GET_ISSUE_QUERY, IssueDao::class.java, Pair("iid", issueId))
}