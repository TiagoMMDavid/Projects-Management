package pt.isel.daw.g08.project.controllers

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.ERROR_HREF
import pt.isel.daw.g08.project.responses.ProblemJson
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.toResponseEntity
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@RestController
class ErrorController : ErrorController {

    @GetMapping(ERROR_HREF)
    fun handleError(
        request: HttpServletRequest,
        @RequestAttribute(name = RequestDispatcher.ERROR_STATUS_CODE) status: Int
    ): ResponseEntity<Response> {
        val httpStatus = HttpStatus.valueOf(status)

        return ProblemJson(
            httpStatus.name,
            httpStatus.name,
            httpStatus.value(),
            httpStatus.reasonPhrase,
            request.requestURI
        ).toResponseEntity(httpStatus)
    }

    override fun getErrorPath() = ERROR_HREF
}