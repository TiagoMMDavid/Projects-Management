package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

data class ProjectOutputModel(
    val name: String,
    val description: String,
)

data class ProjectsOutputModel(
    val projects: List<ProjectOutputModel>,
)

data class ProjectInputModel(
    val description: String,
)

data class ProjectPutResponseModel(
    val status: String,             // Created or Modified
    val projectDetails: String,
)

@RestController
@RequestMapping("/api/projects")
class ProjectsController(val jdbi: Jdbi) {

    @GetMapping()
    fun getAllProjects(): ProjectsOutputModel {
        TODO()
    }

    @GetMapping("{projectName}")
    fun getProject (
            @PathVariable projectName: String,
    ): ProjectOutputModel {
        TODO()
    }

    @PutMapping("{projectName}")
    fun putProject(
            @PathVariable projectName: String,
            @RequestBody input: ProjectInputModel,
    ): ProjectPutResponseModel {
        TODO()
    }
}