package pt.isel.daw.g08.project.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.utils.EnvironmentInfo
import java.net.URI

const val PROJECTS_HREF = "api/projects"
const val USERS_HREF = "api/users"

const val COUNT_DEFAULT_VALUE = "10"
const val PAGE_DEFAULT_VALUE = "0"

val INPUT_CONTENT_TYPE = MediaType.APPLICATION_FORM_URLENCODED

abstract class BaseController {
    companion object {
        fun createResponseEntity(response: Response, status: Int) = ResponseEntity
            .status(status)
            .contentType(MediaType.parseMediaType(response.getContentType()))
            .body(response)

        fun createUriListForPagination(baseUri: String, pageIndex: Int, pageSize: Int, count: Int, collectionSize: Int): List<SirenLink> {
            val toReturn = mutableListOf(
                SirenLink(listOf("self"), URI("${baseUri}?page=${pageIndex}&count=${count}")),
                SirenLink(listOf("page"), hrefTemplate = "${baseUri}{?pageIndex,pageSize}")
            )

            if (pageIndex > 0)
                toReturn.add(SirenLink(listOf("previous"), URI("${baseUri}?page=${pageIndex - 1}&count=${count}")))
            if (collectionSize != ((pageIndex + 1) * pageSize))
                toReturn.add(SirenLink(listOf("next"), URI("${baseUri}?page=${pageIndex + 1}&count=${count}")))
            return toReturn
        }
    }

    @Autowired
    protected lateinit var env: EnvironmentInfo
}