package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.PROJECTS_HREF
import pt.isel.daw.g08.project.Routes.PROJECT_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getIssuesUri
import pt.isel.daw.g08.project.Routes.getLabelsUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getStatesUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.controllers.models.ProjectCreateInputModel
import pt.isel.daw.g08.project.controllers.models.ProjectOutputModel
import pt.isel.daw.g08.project.controllers.models.ProjectsOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.ProjectsDb
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.pipeline.interceptors.USER_ATTRIBUTE
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.toResponseEntity
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
class ProjectsController(val db: ProjectsDb) {

    @GetMapping(PROJECTS_HREF)
    fun getAllProjects(
        pagination: Pagination
    ): ResponseEntity<Response> {
        val projects = db.getAllProjects(pagination.page, pagination.limit)

        val collectionSize = db.getProjectsCount()
        val projectsModel = ProjectsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = projects.size
        )

        return projectsModel.toSirenObject(
            entities = projects.map { project ->
                ProjectOutputModel(
                    id = project.pid,
                    name = project.name,
                    description = project.description,
                    author = project.author_name
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getProjectByIdUri(project.pid).includeHost()),
                        SirenLink(rel = listOf("labels"), href = getLabelsUri(project.pid).includeHost()),
                        SirenLink(rel = listOf("issues"), href = getIssuesUri(project.pid).includeHost()),
                        SirenLink(rel = listOf("states"), href = getStatesUri(project.pid).includeHost()),
                        SirenLink(rel = listOf("author"), href = getUserByIdUri(project.author_id).includeHost()),
                        SirenLink(rel = listOf("projects"), href = URI(PROJECTS_HREF).includeHost())
                    ),
                )
            },
            actions = listOf(
                SirenAction(
                    name = "create-project",
                    title = "Create Project",
                    method = HttpMethod.PUT,
                    href = URI(PROJECTS_HREF).includeHost(),
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "name", type = text),
                        SirenActionField(name = "description", type = text)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                URI(PROJECTS_HREF).includeHost(),
                pagination.page,
                projectsModel.pageSize,
                pagination.limit,
                collectionSize)
        ).toResponseEntity(HttpStatus.OK)
    }

    @GetMapping(PROJECT_BY_ID_HREF)
    fun getProject(
        @PathVariable projectId: Int,
    ): ResponseEntity<Response> {
        val project = db.getProjectById(projectId)
        val projectModel = ProjectOutputModel(
            id = project.pid,
            name = project.name,
            description = project.description,
            author = project.author_name
        )

        val selfUri = getProjectByIdUri(projectId).includeHost()

        return projectModel.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-project",
                        title = "Edit Project",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectModel.id),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "description", type = text),
                        )
                    ),
                    SirenAction(
                        name = "delete-project",
                        title = "Delete Project",
                        method = HttpMethod.DELETE,
                        href = selfUri,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectModel.id),
                        )
                    )
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = selfUri),
                    SirenLink(rel = listOf("labels"), href = getLabelsUri(project.pid).includeHost()),
                    SirenLink(rel = listOf("issues"), href = getIssuesUri(project.pid).includeHost()),
                    SirenLink(rel = listOf("states"), href = getStatesUri(project.pid).includeHost()),
                    SirenLink(rel = listOf("author"), href = getUserByIdUri(project.author_id).includeHost()),
                    SirenLink(rel = listOf("projects"), href = URI(PROJECTS_HREF).includeHost())
                ),
            ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PutMapping(PROJECTS_HREF)
    fun createProject(
        input: ProjectCreateInputModel,
        user: User
    ): ResponseEntity<Any> {
        if (input.name != null && input.description != null) {
            val projectId = db.createProject(input.name, input.description, user.uid)
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", getProjectByIdUri(projectId).includeHost().toString())
                .body(null)
        } else {
            throw InvalidInputException("Missing name and/or description")
        }
    }

    @RequiresAuth
    @PutMapping(PROJECT_BY_ID_HREF)
    fun editProject(
        @PathVariable projectId: Int,
        input: ProjectCreateInputModel,
        user: User
    ): ResponseEntity<Any> {
        if (input.name != null || input.description != null) {
            db.editProject(input.name, input.description, projectId)
            return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", getProjectByIdUri(projectId).includeHost().toString())
                .body(null)
        } else {
            throw InvalidInputException("Missing new name or new description")
        }
    }
}