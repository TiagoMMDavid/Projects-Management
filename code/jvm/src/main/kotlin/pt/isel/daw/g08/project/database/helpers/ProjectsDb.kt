package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Project

private const val GET_PROJECTS_BASE = "SELECT pid, name, description, author_id, author_name FROM V_PROJECT"
private const val GET_ALL_PROJECTS_QUERY = "$GET_PROJECTS_BASE ORDER BY pid"
private const val GET_PROJECTS_COUNT = "SELECT COUNT(pid) as count FROM PROJECT"
private const val GET_PROJECT_QUERY = "$GET_PROJECTS_BASE WHERE pid = :pid"

private const val CREATE_PROJECT_QUERY = "INSERT INTO PROJECT(name, description, author) VALUES(:name, :description, :author)"
private const val UPDATE_PROJECT_START = "UPDATE PROJECT SET"
private const val UPDATE_PROJECT_END = "WHERE pid = :pid"

private const val DELETE_PROJECT_QUERY = "DELETE FROM PROJECT WHERE pid = :pid"

@Component
class ProjectsDb(val jdbi: Jdbi) {
    fun getAllProjects(page: Int, perPage: Int) = jdbi.getList(GET_ALL_PROJECTS_QUERY, Project::class.java, page, perPage)
    fun getProjectsCount(): Int = jdbi.getOne(GET_PROJECTS_COUNT, Int::class.java)
    fun getProjectById(projectId: Int): Project = jdbi.getOne(GET_PROJECT_QUERY, Project::class.java, mapOf("pid" to projectId))

    fun createProject(name: String, description: String, userId: Int) =
        jdbi.insertAndGet(
            CREATE_PROJECT_QUERY, Int::class.java,
            GET_PROJECT_QUERY, Project::class.java,
            mapOf(
                "name" to name,
                "description" to description,
                "author" to userId
            ),
            "pid"
        )

    fun editProject(name: String? = null, description: String? = null, projectId: Int) {
        if (name == null && description == null) {
            return
        }
        val updateFields = mutableMapOf<String, Any>()
        if (name != null) updateFields["name"] = name
        if (description != null) updateFields["description"] = description

        jdbi.update(
            UPDATE_PROJECT_START,
            updateFields,
            UPDATE_PROJECT_END,
            mapOf("pid" to projectId)
        )
    }

    fun deleteProject(projectId: Int) = jdbi.delete(DELETE_PROJECT_QUERY, mapOf("pid" to projectId))
}