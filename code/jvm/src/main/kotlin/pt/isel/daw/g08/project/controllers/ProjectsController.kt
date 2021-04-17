package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.ProjectCreateInputModel
import pt.isel.daw.g08.project.controllers.models.ProjectEditInputModel
import pt.isel.daw.g08.project.controllers.models.ProjectOutputModel
import pt.isel.daw.g08.project.controllers.models.ProjectsOutputModel
import pt.isel.daw.g08.project.database.helpers.ProjectsDb
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping(PROJECTS_HREF)
class ProjectsController(val db: ProjectsDb) : BaseController() {

    @GetMapping
    fun getAllProjects(
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val projectsDao = db.getAllProjects(page, count)
        val collectionSize = db.getProjectsCount()
        val projects = ProjectsOutputModel(
            collectionSize = collectionSize,
            pageIndex = page,
            pageSize = projectsDao.size
        )

        val projectsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}"

        return createResponseEntity(
            projects.toSirenObject(
                entities = projectsDao.map { projectDao ->
                    ProjectOutputModel(
                        id = projectDao.pid,
                        name = projectDao.name,
                        description = projectDao.description,
                        author = projectDao.author_name
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = URI("${projectsUri}/${projectDao.pid}")),
                            SirenLink(rel = listOf("labels"), href = URI("${projectsUri}/${projectDao.pid}/labels")),
                            SirenLink(rel = listOf("issues"), href = URI("${projectsUri}/${projectDao.pid}/issues")),
                            SirenLink(rel = listOf("states"), href = URI("${projectsUri}/${projectDao.pid}/states")),
                            SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${projectDao.author_id}")),
                            SirenLink(rel = listOf("projects"), href = URI(projectsUri))
                        ),
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-project",
                        title = "Create Project",
                        method = HttpMethod.PUT,
                        href = URI(projectsUri),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "description", type = text)
                        )
                    )
                ),
                links = createUriListForPagination(projectsUri, page, projects.pageSize, count, collectionSize)
            ),
            200
        )
    }

    @GetMapping("{projectId}")
    fun getProject(
        @PathVariable projectId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val projectDao = db.getProjectById(projectId)
        val project = ProjectOutputModel(
            id = projectDao.pid,
            name = projectDao.name,
            description = projectDao.description,
            author = projectDao.author_name
        )
        val selfUri = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${project.id}")
        val baseUri = "${env.getBaseUrl()}/${PROJECTS_HREF}"

        return createResponseEntity(
            project.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-project",
                        title = "Edit Project",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = project.id),
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
                            SirenActionField(name = "projectId", type = hidden, value = project.id),
                        )
                    )
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = selfUri),
                    SirenLink(rel = listOf("labels"), href = URI("${baseUri}/${project.id}/labels")),
                    SirenLink(rel = listOf("issues"), href = URI("${baseUri}/${project.id}/states")),
                    SirenLink(rel = listOf("states"), href = URI("${baseUri}/${project.id}/issues")),
                    SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${projectDao.author_id}")),
                    SirenLink(rel = listOf("projects"), href = URI(baseUri))
                ),
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
        @PathVariable projectId: String,
        @RequestBody input: ProjectEditInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }
}