package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.ProjectDao
import java.lang.StringBuilder

private const val GET_PROJECTS_BASE = "SELECT pid, name, description, author_id, author_name FROM V_PROJECT"
private const val GET_ALL_PROJECTS_QUERY = "$GET_PROJECTS_BASE ORDER BY pid"
private const val GET_PROJECTS_COUNT = "SELECT COUNT(pid) as count FROM PROJECT"
private const val GET_PROJECT_QUERY = "$GET_PROJECTS_BASE WHERE pid = :pid"

private const val CREATE_PROJECT_QUERY = "INSERT INTO PROJECT(name, description, author) VALUES(:name, :description, :author)"
private const val UPDATE_PROJECT_START = "UPDATE PROJECT SET"
private const val UPDATE_PROJECT_END = "WHERE pid = :pid"

@Component
class ProjectsDb : DatabaseHelper() {
    fun getAllProjects(page: Int, perPage: Int) = getList(GET_ALL_PROJECTS_QUERY, ProjectDao::class.java, page, perPage)
    fun getProjectsCount(): Int = getOne(GET_PROJECTS_COUNT, Int::class.java)
    fun getProjectById(projectId: Int): ProjectDao = getOne(GET_PROJECT_QUERY, ProjectDao::class.java, Pair("pid", projectId))

    fun createProject(name: String, description: String, userId: Int) =
        insert(CREATE_PROJECT_QUERY, Int::class.java,
            Pair("name", name),
            Pair("description", description),
            Pair("author", userId)
        )

    fun editProject(name: String? = null, description: String? = null, projectId: Int) {
        if (name == null && description == null) {
            return
        }
        val updateFields = mutableListOf<Pair<String, Any>>()
        if (name != null) updateFields.add(Pair("name", name))
        if (description != null) updateFields.add(Pair("description", description))

        update(
            UPDATE_PROJECT_START,
            updateFields,
            UPDATE_PROJECT_END,
            listOf(Pair("pid", projectId))
        )
    }
}