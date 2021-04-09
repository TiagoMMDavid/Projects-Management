package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*
import pt.isel.daw.g08.project.dao.ProjectDao
import pt.isel.daw.g08.project.utils.urlDecode
import pt.isel.daw.g08.project.utils.urlEncode

private const val GET_ALL_PROJECTS_QUERY = "SELECT name, description FROM PROJECT"
private const val GET_PROJECT_QUERY = "SELECT name, description FROM PROJECT WHERE name = :name"

@RestController
@RequestMapping("/api/projects")
class ProjectsController(val jdbi: Jdbi) : BaseController() {

    // URL roots
    val projectsRoot: String by lazy {
        "${env.getBaseUrl()}/api/projects"
    }

    @GetMapping
    fun getAllProjects(): ProjectsOutputModel {
        val projects = jdbi.withHandle<List<ProjectDao>, Exception> {
            it.createQuery(GET_ALL_PROJECTS_QUERY)
                .mapTo(ProjectDao::class.java)
                .list()
        }

        return ProjectsOutputModel(projects.map {
            val encodedProject = it.name.urlEncode()
            ProjectOutputModel(
                name = it.name,
                description = it.description,
                labelsUrl = "${projectsRoot}/${encodedProject}/labels",
                issuesUrl = "${projectsRoot}/${encodedProject}/issues",
                statesUrl = "${projectsRoot}/${encodedProject}/states",
            )
        })
    }

    @GetMapping("{projectName}")
    fun getProject(
        @PathVariable projectName: String,
    ): ProjectOutputModel {
        val project = jdbi.withHandle<ProjectDao, Exception> {
            it.createQuery(GET_PROJECT_QUERY)
                .bind("name", projectName.urlDecode())
                .mapTo(ProjectDao::class.java)
                .one()
        }

        val encodedProject = project.name.urlEncode()
        return ProjectOutputModel(
            name = project.name,
            description = project.description,
            labelsUrl = "${projectsRoot}/${encodedProject}/labels",
            issuesUrl = "${projectsRoot}/${encodedProject}/issues",
            statesUrl = "${projectsRoot}/${encodedProject}/states",
        )
    }

    @PutMapping
    fun createProject(
        @RequestBody input: ProjectCreateInputModel,
    ): ProjectCreateOutputModel {
        TODO()
    }

    @PutMapping("{projectName}")
    fun editProject(
        @PathVariable projectName: String,
        @RequestBody input: ProjectEditInputModel,
    ): ProjectCreateOutputModel {
        TODO()
    }
}