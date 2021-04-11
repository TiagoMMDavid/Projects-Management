package pt.isel.daw.g08.project.controllers

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pt.isel.daw.g08.project.responses.Response

const val PROJECTS_HREF = "/api/projects"

class ControllerHelper {
    companion object {
        fun createResponseEntity(response: Response, status: Int) = ResponseEntity
            .status(status)
            .contentType(MediaType.parseMediaType(response.getContentType()))
            .body(response)
    }
}