package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Issue

private const val GET_ISSUES_BASE =
    "SELECT iid, number, name, description, create_date, close_date, state_id, state_name, state_number, project_id, project_name, author_id, author_name FROM V_ISSUE"

private const val GET_ISSUES_FROM_PROJECT_QUERY = "$GET_ISSUES_BASE WHERE project_id = :pid ORDER BY number"
private const val GET_ISSUES_COUNT = "SELECT COUNT(iid) as count FROM ISSUE WHERE project = :pid"
private const val GET_ISSUE_QUERY = "$GET_ISSUES_BASE WHERE project_id = :projectId AND number = :issueNumber"
private const val GET_ISSUE_BY_ID_QUERY = "$GET_ISSUES_BASE WHERE iid = :iid"

private const val UPDATE_ISSUE_START = "UPDATE ISSUE SET"
private const val UPDATE_ISSUE_END = "WHERE project = :projectId AND number = :number"

private const val CREATE_ISSUE_QUERY = "INSERT INTO ISSUE(project, name, description, author) VALUES(:project, :name, :description, :author)"

private const val DELETE_ISSUE_QUERY = "DELETE FROM ISSUE WHERE project = :projectId AND number = :issueNumber"

@Component
class IssuesDb(
    val projectsDb: ProjectsDb,
    val statesDb: StatesDb,
    val jdbi: Jdbi,
) {
    fun getAllIssuesFromProject(page: Int, perPage: Int, projectId: Int): List<Issue> {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.getList(GET_ISSUES_FROM_PROJECT_QUERY, Issue::class.java, page, perPage, mapOf("pid" to projectId))
    }

    fun getIssuesCount(projectId: Int) = jdbi.getOne(GET_ISSUES_COUNT, Int::class.java, mapOf("pid" to projectId))
    fun getIssueByNumber(projectId: Int, issueNumber: Int) =
        jdbi.getOne(GET_ISSUE_QUERY, Issue::class.java,
            mapOf(
                "projectId" to projectId,
                "issueNumber" to issueNumber
            )
        )

    fun editIssue(projectId: Int, issueNumber: Int, name: String?, description: String?, stateName: String?) {
        if (name == null && description == null && stateName == null) {
            return
        }

        val updateFields = mutableMapOf<String, Any>()
        if (name != null) updateFields["name"] = name
        if (description != null) updateFields["description"] = description
        if (stateName != null) updateFields["state"] = statesDb.getStateByName(projectId, stateName).sid

        jdbi.update(
            UPDATE_ISSUE_START,
            updateFields,
            UPDATE_ISSUE_END,
            mapOf("projectId" to projectId, "number" to issueNumber)
        )
    }

    fun createIssue(projectId: Int, name: String, description: String, userId: Int): Issue {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.insertAndGet(
            CREATE_ISSUE_QUERY, Int::class.java,
            GET_ISSUE_BY_ID_QUERY, Issue::class.java,
            mapOf(
                "project" to projectId,
                "name" to name,
                "description" to description,
                "author" to userId
            ),
            "iid"
        )
    }

    fun deleteIssue(projectId: Int, issueNumber: Int) =
        jdbi.delete(DELETE_ISSUE_QUERY,
            mapOf(
                "projectId" to projectId,
                "issueNumber" to issueNumber
            )
        )
}