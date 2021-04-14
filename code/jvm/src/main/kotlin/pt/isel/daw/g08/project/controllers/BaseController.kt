package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.utils.EnvironmentInfo

const val PROJECTS_HREF = "/api/projects"

abstract class BaseController {
    companion object {
        fun createResponseEntity(response: Response, status: Int) = ResponseEntity
            .status(status)
            .contentType(MediaType.parseMediaType(response.getContentType()))
            .body(response)
    }

    @Autowired
    protected lateinit var env: EnvironmentInfo

    @Autowired
    protected lateinit var jdbi: Jdbi
}