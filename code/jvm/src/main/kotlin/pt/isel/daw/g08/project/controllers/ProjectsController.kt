package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.ProjectCreateInputModel
import pt.isel.daw.g08.project.controllers.models.ProjectEditInputModel
import pt.isel.daw.g08.project.controllers.models.ProjectOutputModel
import pt.isel.daw.g08.project.controllers.models.ProjectsOutputModel
import pt.isel.daw.g08.project.database.helpers.ProjectsDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping(PROJECTS_HREF)
class ProjectsController(val db: ProjectsDb) : BaseController() {

    @GetMapping
    fun getAllProjects(
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val projects = db.getAllProjects(page, count)
        val collectionSize = db.getProjectsCount()

        return createResponseEntity(
            ProjectsOutputModel(
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = projects.size,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}{?pageIndex,pageSize}",
                projects = projects.map {
                    ProjectOutputModel(
                        id = it.pid,
                        name = it.name,
                        description = it.description,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${it.pid}",
                        labelsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${it.pid}/labels",
                        issuesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${it.pid}/issues",
                        statesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${it.pid}/states",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        projectsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}",
                        isCollectionItem = true
                    )
            }),
            200
        )
    }

    @GetMapping("{projectId}")
    fun getProject(
        @PathVariable projectId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val project = db.getProjectById(projectId)

        return createResponseEntity(
            ProjectOutputModel(
                id = project.pid,
                name = project.name,
                description = project.description,
                authorName = project.author_name,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${project.pid}",
                labelsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${project.pid}/labels",
                issuesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${project.pid}/issues",
                statesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${project.pid}/states",
                authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${project.author_id}",
                projectsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}"
            ),
            200
        )
    }

    @PutMapping
    fun createProject(
        @RequestBody input: ProjectCreateInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }

    @PutMapping("{projectId}")
    fun editProject(
        @PathVariable projectName: String,
        @RequestBody input: ProjectEditInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }
}