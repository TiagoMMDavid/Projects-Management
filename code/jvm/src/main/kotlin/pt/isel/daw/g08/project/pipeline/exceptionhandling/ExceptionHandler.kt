package pt.isel.daw.g08.project.pipeline.exceptionhandling

import org.jdbi.v3.core.JdbiException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.isel.daw.g08.project.controllers.BaseController.Companion.createResponseEntity
import pt.isel.daw.g08.project.database.PsqlErrorCode
import pt.isel.daw.g08.project.exceptions.AuthorizationException
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.exceptions.NotFoundException
import pt.isel.daw.g08.project.exceptions.PaginationException
import pt.isel.daw.g08.project.responses.ProblemJson
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.utils.EnvironmentInfo
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

private val logger = LoggerFactory.getLogger("ExceptionHandler")

fun handleExceptionResponse(
    type: String,
    title: String,
    status: HttpStatus,
    detail: String,
    instance: String
): ResponseEntity<Response> {
    logger.error("[$instance] $detail")

    return ResponseEntity
        .status(status)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(ProblemJson(type, title, status.value(), detail, instance))
}

@RestControllerAdvice
class ExceptionHandler {

    @Autowired
    private lateinit var env: EnvironmentInfo

    @ExceptionHandler(value = [NotFoundException::class])
    private fun handleNotFoundException(
        ex: NotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        return handleExceptionResponse(
            "${env.getBaseUrl()}/problems/resource-not-found",
            "Resource Not Found",
            HttpStatus.NOT_FOUND,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [AuthorizationException::class])
    private fun handleAuthorizationException(
        ex: AuthorizationException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        return handleExceptionResponse(
            "${env.getBaseUrl()}/problems/not-authorized",
            "Authorization Error",
            HttpStatus.FORBIDDEN,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [PaginationException::class])
    private fun handlePaginationException(
        ex: PaginationException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        return handleExceptionResponse(
            "${env.getBaseUrl()}/problems/invalid-pagination-parameters",
            "Invalid Pagination Parameters",
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [InvalidInputException::class])
    private fun handleInvalidInputException(
        ex: InvalidInputException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        return handleExceptionResponse(
            "${env.getBaseUrl()}/problems/invalid-input",
            "Invalid Input",
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage,
            request.requestURI
        )
    }

    @ExceptionHandler(value = [JdbiException::class])
    private fun handleJdbiException(
        ex: JdbiException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        val cause = ex.cause as SQLException
        val psqlError = PsqlErrorCode.values().find { it.code == cause.sqlState }

        if (psqlError == null) {
            return handleExceptionResponse(
                "${env.getBaseUrl()}/problems/database-error",
                "Unknown Database Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unknown database error has occurred",
                request.requestURI
            )
        }

        return when (psqlError) {
            PsqlErrorCode.UniqueViolation -> {
                handleExceptionResponse(
                    "${env.getBaseUrl()}/problems/resource-already-exists",
                    "Resource Already Exists",
                    HttpStatus.CONFLICT,
                    cause.localizedMessage,
                    request.requestURI
                )
            }
        }
    }
}