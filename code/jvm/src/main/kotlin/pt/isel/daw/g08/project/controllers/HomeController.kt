package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.BASE_URI
import pt.isel.daw.g08.project.Routes.COMMENTS_HREF
import pt.isel.daw.g08.project.Routes.COMMENT_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.ISSUES_HREF
import pt.isel.daw.g08.project.Routes.ISSUE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.LABELS_HREF
import pt.isel.daw.g08.project.Routes.LABELS_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.NEXT_STATES_HREF
import pt.isel.daw.g08.project.Routes.PROJECTS_HREF
import pt.isel.daw.g08.project.Routes.PROJECT_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.STATES_HREF
import pt.isel.daw.g08.project.Routes.STATE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.USERS_HREF
import pt.isel.daw.g08.project.Routes.USER_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.USER_HREF
import pt.isel.daw.g08.project.controllers.models.HomeOutputModel
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity
import java.lang.management.ManagementFactory
import java.net.URI
import java.time.OffsetDateTime

@RestController
class HomeController {

    @GetMapping(BASE_URI)
    fun getHome(): ResponseEntity<Response> {
        return HomeOutputModel(
            name = "DAW Project",
            group = "LI61D-G08",
            time = OffsetDateTime.now(),
            uptimeMs = ManagementFactory.getRuntimeMXBean().uptime
        ).toSirenObject(
            links = listOf(
                SirenLink(rel = listOf("projects"), href = URI(PROJECTS_HREF)),
                SirenLink(rel = listOf("users"), href = URI(USERS_HREF)),
                SirenLink(rel = listOf("project"), hrefTemplate = PROJECT_BY_ID_HREF),
                SirenLink(rel = listOf("labels"), hrefTemplate = LABELS_HREF),
                SirenLink(rel = listOf("label"), hrefTemplate = LABEL_BY_NUMBER_HREF),
                SirenLink(rel = listOf("states"), hrefTemplate = STATES_HREF),
                SirenLink(rel = listOf("state"), hrefTemplate = STATE_BY_NUMBER_HREF),
                SirenLink(rel = listOf("nextStates"), hrefTemplate = NEXT_STATES_HREF),
                SirenLink(rel = listOf("issues"), hrefTemplate = ISSUES_HREF),
                SirenLink(rel = listOf("issue"), hrefTemplate = ISSUE_BY_NUMBER_HREF),
                SirenLink(rel = listOf("issueLabels"), hrefTemplate = LABELS_OF_ISSUE_HREF),
                SirenLink(rel = listOf("comments"), hrefTemplate = COMMENTS_HREF),
                SirenLink(rel = listOf("comment"), hrefTemplate = COMMENT_BY_NUMBER_HREF),
                SirenLink(rel = listOf("user"), hrefTemplate = USER_BY_ID_HREF),
                SirenLink(rel = listOf("authUser"), href = URI(USER_HREF)),
            )
        ).toResponseEntity(HttpStatus.OK)
    }
}