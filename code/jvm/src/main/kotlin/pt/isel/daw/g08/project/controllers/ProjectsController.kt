package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*
import pt.isel.daw.g08.project.dao.ProjectDao
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.utils.urlDecode
import pt.isel.daw.g08.project.utils.urlEncode

private const val GET_ALL_PROJECTS_QUERY = "SELECT name, description FROM PROJECT"
private const val GET_PROJECT_QUERY = "SELECT name, description FROM PROJECT WHERE name = :name"

@RestController
@RequestMapping(PROJECTS_HREF)
class ProjectsController(val jdbi: Jdbi) {

    @GetMapping
    fun getAllProjects(): ResponseEntity<Response> {
        val projects = jdbi.withHandle<List<ProjectDao>, Exception> {
            it.createQuery(GET_ALL_PROJECTS_QUERY)
                .mapTo(ProjectDao::class.java)
                .list()
        }

        return ControllerHelper.createResponseEntity(
            ProjectsOutputModel(
                selfUrl = PROJECTS_HREF,
                totalProjects = projects.size,
                projects = projects.map { project ->
                val encodedProject = project.name.urlEncode()
                    ProjectOutputModel(
                        name = project.name,
                        description = project.description,
                        selfUrl = "${PROJECTS_HREF}/${encodedProject}",
                        labelsUrl = "${PROJECTS_HREF}/${encodedProject}/labels",
                        issuesUrl = "${PROJECTS_HREF}/${encodedProject}/issues",
                        statesUrl = "${PROJECTS_HREF}/${encodedProject}/states",
                        projectsUrl = PROJECTS_HREF,
                    )
            }),
            200
        )
    }

    @GetMapping("{projectName}")
    fun getProject(
        @PathVariable projectName: String,
    ): ResponseEntity<Response> {
        val project = jdbi.withHandle<ProjectDao, Exception> {
            it.createQuery(GET_PROJECT_QUERY)
                .bind("name", projectName.urlDecode())
                .mapTo(ProjectDao::class.java)
                .one()
        }

        val encodedProject = project.name.urlEncode()
        return ControllerHelper.createResponseEntity(
            ProjectOutputModel(
                name = project.name,
                description = project.description,
                selfUrl = "${PROJECTS_HREF}/${encodedProject}",
                labelsUrl = "${PROJECTS_HREF}/${encodedProject}/labels",
                issuesUrl = "${PROJECTS_HREF}/${encodedProject}/issues",
                statesUrl = "${PROJECTS_HREF}/${encodedProject}/states",
                projectsUrl = PROJECTS_HREF,
            ),
            200
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