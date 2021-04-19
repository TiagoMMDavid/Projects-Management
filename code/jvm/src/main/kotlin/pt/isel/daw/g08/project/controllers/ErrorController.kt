package pt.isel.daw.g08.project.controllers

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestAttribute
import pt.isel.daw.g08.project.controllers.BaseController.Companion.createResponseEntity
import pt.isel.daw.g08.project.responses.ProblemJson
import pt.isel.daw.g08.project.responses.Response

import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

private const val ERROR_HREF = "error"

@RestController
@RequestMapping(ERROR_HREF)
class ErrorController : ErrorController {

    @GetMapping
    fun handleError(
        request: HttpServletRequest,
        @RequestAttribute(name = RequestDispatcher.ERROR_STATUS_CODE) status: Int
    ): ResponseEntity<Response> {
        val httpStatus = HttpStatus.valueOf(status)
        return createResponseEntity(
            ProblemJson(httpStatus.name, httpStatus.name, httpStatus.value(), httpStatus.reasonPhrase, request.requestURI),
            HttpStatus.valueOf(status)
        )
    }

    override fun getErrorPath() = ERROR_HREF
}