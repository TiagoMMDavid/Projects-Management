package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.ProjectDao

private const val GET_ALL_PROJECTS_QUERY = "SELECT pid, name, description, author_id, author_name FROM V_PROJECT"
private const val GET_PROJECTS_COUNT = "SELECT COUNT(pid) as count FROM PROJECT"
private const val GET_PROJECT_QUERY = "$GET_ALL_PROJECTS_QUERY WHERE pid = :pid"

@Component
class ProjectsDb : DatabaseHelper() {
    fun getAllProjects(page: Int, perPage: Int) = getList(page, perPage, GET_ALL_PROJECTS_QUERY, ProjectDao::class.java)
    fun getProjectsCount(): Int = getOne(GET_PROJECTS_COUNT, Int::class.java)
    fun getProjectById(projectId: Int): ProjectDao = boundedGetOne("pid", projectId, GET_PROJECT_QUERY, ProjectDao::class.java)
}