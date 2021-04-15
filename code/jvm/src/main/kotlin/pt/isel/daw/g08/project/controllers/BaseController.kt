package pt.isel.daw.g08.project.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.utils.EnvironmentInfo

const val PROJECTS_HREF = "api/projects"
const val USERS_HREF = "api/users"

abstract class BaseController {
    companion object {
        fun createResponseEntity(response: Response, status: Int) = ResponseEntity
            .status(status)
            .contentType(MediaType.parseMediaType(response.getContentType()))
            .body(response)
    }

    @Autowired
    protected lateinit var env: EnvironmentInfo
}