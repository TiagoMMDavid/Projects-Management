package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.BASE_URI
import pt.isel.daw.g08.project.Routes.COMMENTS_HREF
import pt.isel.daw.g08.project.Routes.COMMENT_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.HOST
import pt.isel.daw.g08.project.Routes.ISSUES_HREF
import pt.isel.daw.g08.project.Routes.ISSUE_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.LABELS_HREF
import pt.isel.daw.g08.project.Routes.LABELS_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.NEXT_STATES_HREF
import pt.isel.daw.g08.project.Routes.PROJECTS_HREF
import pt.isel.daw.g08.project.Routes.PROJECT_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.SCHEME
import pt.isel.daw.g08.project.Routes.STATES_HREF
import pt.isel.daw.g08.project.Routes.STATE_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.USERS_HREF
import pt.isel.daw.g08.project.Routes.USER_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.includeHost
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
        val hostString = "${SCHEME.toLowerCase()}://${HOST}"
        return HomeOutputModel(
            name = "DAW Project",
            group = "LI61D-G08",
            time = OffsetDateTime.now(),
            uptimeMs = ManagementFactory.getRuntimeMXBean().uptime
        ).toSirenObject(
            links = listOf(
                SirenLink(rel = listOf("projects"), href = URI(PROJECTS_HREF).includeHost()),
                SirenLink(rel = listOf("users"), href = URI(USERS_HREF).includeHost()),
                SirenLink(rel = listOf("project"), hrefTemplate = "${hostString}${PROJECT_BY_ID_HREF}"),
                SirenLink(rel = listOf("labels"), hrefTemplate = "${hostString}${LABELS_HREF}"),
                SirenLink(rel = listOf("label"), hrefTemplate = "${hostString}${LABEL_BY_ID_HREF}"),
                SirenLink(rel = listOf("states"), hrefTemplate = "${hostString}${STATES_HREF}"),
                SirenLink(rel = listOf("state"), hrefTemplate = "${hostString}${STATE_BY_ID_HREF}"),
                SirenLink(rel = listOf("nextStates"), hrefTemplate = "${hostString}${NEXT_STATES_HREF}"),
                SirenLink(rel = listOf("issues"), hrefTemplate = "${hostString}${ISSUES_HREF}"),
                SirenLink(rel = listOf("issue"), hrefTemplate = "${hostString}${ISSUE_BY_ID_HREF}"),
                SirenLink(rel = listOf("issueLabels"), hrefTemplate = "${hostString}${LABELS_OF_ISSUE_HREF}"),
                SirenLink(rel = listOf("comments"), hrefTemplate = "${hostString}${COMMENTS_HREF}"),
                SirenLink(rel = listOf("comment"), hrefTemplate = "${hostString}${COMMENT_BY_ID_HREF}"),
                SirenLink(rel = listOf("user"), hrefTemplate = "${hostString}${USER_BY_ID_HREF}"),
            )
        ).toResponseEntity(HttpStatus.OK)
    }
}