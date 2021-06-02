package pt.isel.daw.g08.project.pipeline.exceptionhandling

import org.jdbi.v3.core.JdbiException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.isel.daw.g08.project.auth.AuthHeaderValidator.AUTH_SCHEME
import pt.isel.daw.g08.project.database.PsqlErrorCode
import pt.isel.daw.g08.project.database.getPsqlErrorCode
import pt.isel.daw.g08.project.exceptions.AuthorizationException
import pt.isel.daw.g08.project.exceptions.ForbiddenException
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.exceptions.NotFoundException
import pt.isel.daw.g08.project.exceptions.PaginationException
import pt.isel.daw.g08.project.responses.ProblemJson
import pt.isel.daw.g08.project.responses.Response
import java.net.URI
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

private val logger = LoggerFactory.getLogger("ExceptionHandler")

fun handleExceptionResponse(
    type: URI,
    title: String,
    status: HttpStatus,
    detail: String,
    instance: String,
    customHeaders: HttpHeaders = HttpHeaders(),
): ResponseEntity<Response> {
    logger.error("[$instance] $detail")

    return ResponseEntity
        .status(status)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .headers(customHeaders)
        .body(ProblemJson(type.toString(), title, status.value(), detail, instance))
}

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [NotFoundException::class])
    private fun handleNotFoundException(
        ex: NotFoundException,
        request: HttpServletRequest
    ) =
        handleExceptionResponse(
            URI("/problems/resource-not-found"),
            "Resource Not Found",
            HttpStatus.NOT_FOUND,
            ex.localizedMessage,
            request.requestURI
        )

    @ExceptionHandler(value = [AuthorizationException::class])
    private fun handleAuthorizationException(
        ex: AuthorizationException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        val headers = HttpHeaders()
        headers.add("WWW-Authenticate", AUTH_SCHEME)

        return handleExceptionResponse(
            URI("/problems/not-authorized"),
            "Authorization Error",
            HttpStatus.UNAUTHORIZED,
            ex.localizedMessage,
            request.requestURI,
            headers
        )
    }

    @ExceptionHandler(value = [PaginationException::class])
    private fun handlePaginationException(
        ex: PaginationException,
        request: HttpServletRequest
    ) =
        handleExceptionResponse(
            URI("/problems/invalid-pagination-parameters"),
            "Invalid Pagination Parameters",
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage,
            request.requestURI
        )

    @ExceptionHandler(value = [InvalidInputException::class])
    private fun handleInvalidInputException(
        ex: InvalidInputException,
        request: HttpServletRequest
    ) =
        handleExceptionResponse(
            URI("/problems/invalid-input"),
            "Invalid Input",
            HttpStatus.BAD_REQUEST,
            ex.localizedMessage,
            request.requestURI
        )

    @ExceptionHandler(value = [ForbiddenException::class])
    private fun handleForbiddenException(
        ex: ForbiddenException,
        request: HttpServletRequest
    ): ResponseEntity<Response> =
        handleExceptionResponse(
            URI("/problems/forbidden-operation"),
            "Forbidden Operation",
            HttpStatus.FORBIDDEN,
            ex.localizedMessage,
            request.requestURI,
        )

    @ExceptionHandler(value = [JdbiException::class])
    private fun handleJdbiException(
        ex: JdbiException,
        request: HttpServletRequest
    ): ResponseEntity<Response> {
        val cause = ex.cause as SQLException
        val psqlError = ex.getPsqlErrorCode()

        if (psqlError == null) {
            return handleExceptionResponse(
                URI("/problems/database-error"),
                "Unknown Database Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unknown database error has occurred",
                request.requestURI
            )
        }

        return when (psqlError) {
            PsqlErrorCode.UniqueViolation -> {
                handleExceptionResponse(
                    URI("/problems/resource-already-exists"),
                    "Resource Already Exists",
                    HttpStatus.CONFLICT,
                    cause.localizedMessage,
                    request.requestURI
                )
            }
            PsqlErrorCode.ForeignKeyViolation -> {
                handleExceptionResponse(
                    URI("/problems/resource-referenced"),
                    "Resource Is Referenced",
                    HttpStatus.CONFLICT,
                    cause.localizedMessage,
                    request.requestURI
                )
            }
            PsqlErrorCode.CheckViolation -> {
                handleExceptionResponse(
                    URI("/problems/invalid-creation-request"),
                    "Invalid Creation Request",
                    HttpStatus.FORBIDDEN,
                    cause.localizedMessage,
                    request.requestURI
                )
            }
            PsqlErrorCode.StringDataRightTruncation -> {
                handleExceptionResponse(
                    URI("/problems/invalid-string-size"),
                    "Invalid String Size",
                    HttpStatus.BAD_REQUEST,
                    cause.localizedMessage,
                    request.requestURI
                )
            }
            PsqlErrorCode.NoStartState -> {
                handleExceptionResponse(
                    URI("/problems/no-start-state"),
                    "No Start State",
                    HttpStatus.FORBIDDEN,
                    "Can't create issue due to no starting state defined in the project",
                    request.requestURI
                )
            }
            PsqlErrorCode.InvalidStateTransition -> {
                handleExceptionResponse(
                    URI("/problems/invalid-state-transition"),
                    "Invalid State Transition",
                    HttpStatus.FORBIDDEN,
                    "Current state can't transition to the requested state",
                    request.requestURI
                )
            }
            PsqlErrorCode.ArchivedIssue -> {
                handleExceptionResponse(
                    URI("/problems/archived-issue"),
                    "Issue Is Archived",
                    HttpStatus.FORBIDDEN,
                    "Can't add or edit a comment to an archived issue",
                    request.requestURI
                )
            }
            PsqlErrorCode.ForbiddenStateModification -> {
                handleExceptionResponse(
                    URI("/problems/forbidden-state-modification"),
                    "State is reserved",
                    HttpStatus.FORBIDDEN,
                    "Cannot modify the 'closed' and 'archived' states",
                    request.requestURI
                )
            }
        }
    }
}