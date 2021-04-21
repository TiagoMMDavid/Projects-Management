package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Issue

private const val GET_ISSUES_BASE =
    "SELECT iid, number, name, description, create_date, close_date, state_id, state_name, state_number, project_id, project_name, author_id, author_name FROM V_ISSUE"

private const val GET_ISSUES_FROM_PROJECT_QUERY = "$GET_ISSUES_BASE WHERE project_id = :pid ORDER BY number"
private const val GET_ISSUES_COUNT = "SELECT COUNT(iid) as count FROM ISSUE WHERE project = :pid"
const val GET_ISSUE_QUERY = "$GET_ISSUES_BASE WHERE project_id = :projectId AND number = :issueNumber" // Used in LabelsDb

@Component
class IssuesDb(val jdbi: Jdbi) {
    fun getAllIssuesFromProject(page: Int, perPage: Int, projectId: Int) =
        jdbi.getList(GET_ISSUES_FROM_PROJECT_QUERY, Issue::class.java, page, perPage, mapOf("pid" to projectId))

    fun getIssuesCount(projectId: Int) = jdbi.getOne(GET_ISSUES_COUNT, Int::class.java, mapOf("pid" to projectId))
    fun getIssueByNumber(projectId: Int, issueNumber: Int) =
        jdbi.getOne(GET_ISSUE_QUERY, Issue::class.java,
            mapOf(
                "projectId" to projectId,
                "issueNumber" to issueNumber
            )
        )
}