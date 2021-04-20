package pt.isel.daw.g08.project

import org.springframework.http.MediaType
import org.springframework.web.util.UriTemplate
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

object Routes {
    val INPUT_CONTENT_TYPE = MediaType.APPLICATION_FORM_URLENCODED

    const val SCHEME = "http"
    const val PORT = "8080"
    const val HOST = "localhost:$PORT"
    const val BASE_URI = "api"

    const val PAGE_TEMPLATE_QUERY = "{?page,limit}"
    const val PAGE_QUERY = "?page={page}&limit={limit}"

    const val ERROR_HREF = "$BASE_URI/error"

    // Projects
    const val PROJECTS_HREF = "$BASE_URI/projects"
    const val PROJECT_BY_ID_HREF = "$PROJECTS_HREF/{projectId}"

    val PROJECT_BY_ID_HREF_TEMPLATE = UriTemplate(PROJECT_BY_ID_HREF)

    fun getProjectByIdUri(projectId: Int) = PROJECT_BY_ID_HREF_TEMPLATE.expand(projectId)

    // Issues
    const val ISSUES_HREF = "$PROJECT_BY_ID_HREF/issues"
    const val ISSUE_BY_ID_HREF = "$ISSUES_HREF/{issueId}"

    val ISSUES_HREF_TEMPLATE = UriTemplate(ISSUES_HREF)
    val ISSUE_BY_ID_HREF_TEMPLATE = UriTemplate(ISSUE_BY_ID_HREF)

    fun getIssuesUri(projectId: Int) = ISSUES_HREF_TEMPLATE.expand(projectId)
    fun getIssueByIdUri(projectId: Int, issueId: Int) = ISSUE_BY_ID_HREF_TEMPLATE.expand(projectId, issueId)
    
    // Labels
    const val LABELS_HREF = "$PROJECT_BY_ID_HREF/labels"
    const val LABEL_BY_ID_HREF = "$LABELS_HREF/{labelId}"
    const val LABELS_OF_ISSUE_HREF = "$ISSUE_BY_ID_HREF/labels"

    val LABEL_BY_ID_HREF_TEMPLATE = UriTemplate(LABEL_BY_ID_HREF)
    val LABELS_HREF_TEMPLATE = UriTemplate(LABELS_HREF)
    val LABELS_OF_ISSUE_HREF_TEMPLATE = UriTemplate(LABELS_OF_ISSUE_HREF)

    fun getLabelsUri(projectId: Int) = LABELS_HREF_TEMPLATE.expand(projectId)
    fun getLabelByIdUri(projectId: Int, labelId: Int) = LABEL_BY_ID_HREF_TEMPLATE.expand(projectId, labelId)
    fun getLabelsOfIssueUri(projectId: Int, issueId: Int) = LABELS_OF_ISSUE_HREF_TEMPLATE.expand(projectId, issueId)

    // States
    const val STATES_HREF = "$PROJECT_BY_ID_HREF/states"
    const val STATE_BY_ID_HREF = "$STATES_HREF/{stateId}"
    const val NEXT_STATES_HREF = "$STATE_BY_ID_HREF/nextStates"

    val STATE_BY_ID_HREF_TEMPLATE = UriTemplate(STATE_BY_ID_HREF)
    val STATES_HREF_TEMPLATE = UriTemplate(STATES_HREF)
    val NEXT_STATES_HREF_TEMPLATE = UriTemplate(NEXT_STATES_HREF)

    fun getStatesUri(projectId: Int) = STATES_HREF_TEMPLATE.expand(projectId)
    fun getStateByIdUri(projectId: Int, stateId: Int) = STATE_BY_ID_HREF_TEMPLATE.expand(projectId, stateId)
    fun getNextStatesUri(projectId: Int, stateId: Int) = NEXT_STATES_HREF_TEMPLATE.expand(projectId, stateId)
    
    // Comments
    const val COMMENTS_HREF = "$ISSUE_BY_ID_HREF/comments"
    const val COMMENT_BY_ID_HREF = "$COMMENTS_HREF/{commentId}"

    val COMMENTS_HREF_TEMPLATE = UriTemplate(COMMENTS_HREF)
    val COMMENT_BY_ID_HREF_TEMPLATE = UriTemplate(COMMENT_BY_ID_HREF)

    fun getCommentsUri(projectId: Int, issueId: Int) = COMMENTS_HREF_TEMPLATE.expand(projectId, issueId)
    fun getCommentByIdUri(projectId: Int, issueId: Int, commentId: Int) = COMMENT_BY_ID_HREF_TEMPLATE.expand(projectId, issueId, commentId)

    // Users
    const val USERS_HREF = "$BASE_URI/users"
    const val USER_BY_ID_HREF = "$USERS_HREF/{userId}"

    val USER_BY_ID_HREF_TEMPLATE = UriTemplate(USER_BY_ID_HREF)

    fun getUserByIdUri(userId: Int) = USER_BY_ID_HREF_TEMPLATE.expand(userId)
    
    // Helpers
    fun createSirenLinkListForPagination(uri: URI, page: Int, pageSize: Int, limit: Int, collectionSize: Int): List<SirenLink> {
        val toReturn = mutableListOf(
            SirenLink(listOf("self"), UriTemplate("${uri}$PAGE_QUERY").expand(page, limit)),
            SirenLink(listOf("page"), hrefTemplate = "${uri}$PAGE_TEMPLATE_QUERY")
        )

        if (page > 0)
            toReturn.add(
                SirenLink(
                    listOf("previous"),
                    UriTemplate("${uri}$PAGE_QUERY")
                        .expand(page - 1, limit)
                )
            )

        if (collectionSize != ((page + 1) * pageSize))
            toReturn.add(
                SirenLink(
                    listOf("next"),
                    UriTemplate("${uri}$PAGE_QUERY")
                        .expand(page + 1, limit)
                )
            )

        return toReturn
    }

    fun URI.includeHost() = URI(SCHEME.toLowerCase(), HOST, this.path, this.query, this.fragment)
}