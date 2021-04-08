package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import pt.isel.daw.g08.project.controllers.models.*

@RestController
@RequestMapping("/api/projects")
class ProjectsController(val jdbi: Jdbi) {

    @GetMapping
    fun getAllProjects(): ProjectsOutputModel {
        TODO()
    }

    @GetMapping("{projectName}")
    fun getProject(
        @PathVariable projectName: String,
    ): ProjectOutputModel {
        TODO()
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