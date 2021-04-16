package pt.isel.daw.g08.project.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.utils.EnvironmentInfo

const val PROJECTS_HREF = "api/projects"
const val USERS_HREF = "api/users"

const val COUNT_DEFAULT_VALUE = "10"
const val PAGE_DEFAULT_VALUE = "0"

const val INPUT_CONTENT_TYPE = "application/x-www-form-urlencoded"

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