package pt.isel.daw.g08.project

import org.springframework.http.MediaType
import org.springframework.web.util.UriTemplate
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

object Routes {
    val INPUT_CONTENT_TYPE = MediaType.APPLICATION_JSON

    const val SCHEME = "http"
    const val PORT = "8000"
    const val HOST = "localhost:$PORT"
    const val BASE_URI = "/api"

    const val PAGE_TEMPLATE_QUERY = "{?page,limit}"
    const val PAGE_QUERY = "?page={page}&limit={limit}"

    const val PROJECT_PARAM = "projectId"
    const val ISSUE_PARAM = "issueNumber"
    const val SEARCH_PARAM = "q"
    const val EXCLUDE_ISSUE_PARAM = "excludeIssue"
    const val EXCLUDE_STATE_PARAM = "excludeState"
    const val LABEL_PARAM = "labelNumber"
    const val STATE_PARAM = "stateNumber"
    const val NEXT_STATE_PARAM = "nextStateNumber"
    const val COMMENT_PARAM = "commentNumber"
    const val USER_PARAM = "userId"

    // Error
    const val ERROR_HREF = "/error"

    // Projects
    const val PROJECTS_HREF = "$BASE_URI/projects"
    const val PROJECT_BY_ID_HREF = "$PROJECTS_HREF/{$PROJECT_PARAM}"

    val PROJECT_BY_ID_HREF_TEMPLATE = UriTemplate(PROJECT_BY_ID_HREF)

    fun getProjectByIdUri(projectId: Int) = PROJECT_BY_ID_HREF_TEMPLATE.expand(projectId)

    // Issues
    const val ISSUES_HREF = "$PROJECT_BY_ID_HREF/issues"
    const val ISSUE_BY_NUMBER_HREF = "$ISSUES_HREF/{$ISSUE_PARAM}"

    val ISSUES_HREF_TEMPLATE = UriTemplate(ISSUES_HREF)
    val ISSUE_BY_NUMBER_HREF_TEMPLATE = UriTemplate(ISSUE_BY_NUMBER_HREF)

    fun getIssuesUri(projectId: Int) = ISSUES_HREF_TEMPLATE.expand(projectId)
    fun getIssueByNumberUri(projectId: Int, issueNumber: Int) = ISSUE_BY_NUMBER_HREF_TEMPLATE.expand(projectId, issueNumber)
    
    // Labels
    const val LABELS_HREF = "$PROJECT_BY_ID_HREF/labels"
    const val LABEL_BY_NUMBER_HREF = "$LABELS_HREF/{$LABEL_PARAM}"
    const val LABELS_OF_ISSUE_HREF = "$ISSUE_BY_NUMBER_HREF/labels"
    const val LABEL_BY_NUMBER_OF_ISSUE_HREF = "$LABELS_OF_ISSUE_HREF/{$LABEL_PARAM}"

    val LABEL_BY_NUMBER_HREF_TEMPLATE = UriTemplate(LABEL_BY_NUMBER_HREF)
    val LABELS_HREF_TEMPLATE = UriTemplate(LABELS_HREF)
    val LABELS_OF_ISSUE_HREF_TEMPLATE = UriTemplate(LABELS_OF_ISSUE_HREF)
    val LABELS_BY_NUMBER_OF_ISSUE_HREF_TEMPLATE = UriTemplate(LABEL_BY_NUMBER_OF_ISSUE_HREF)

    fun getLabelsUri(projectId: Int) = LABELS_HREF_TEMPLATE.expand(projectId)
    fun getLabelByNumberUri(projectId: Int, labelNumber: Int) = LABEL_BY_NUMBER_HREF_TEMPLATE.expand(projectId, labelNumber)
    fun getLabelsOfIssueUri(projectId: Int, issueNumber: Int) = LABELS_OF_ISSUE_HREF_TEMPLATE.expand(projectId, issueNumber)
    fun getLabelByNumberOfIssueUri(projectId: Int, issueNumber: Int, labelNumber: Int) =
        LABELS_BY_NUMBER_OF_ISSUE_HREF_TEMPLATE.expand(projectId, issueNumber, labelNumber)

    // States
    const val STATES_HREF = "$PROJECT_BY_ID_HREF/states"
    const val STATE_BY_NUMBER_HREF = "$STATES_HREF/{$STATE_PARAM}"
    const val NEXT_STATES_HREF = "$STATE_BY_NUMBER_HREF/nextStates"
    const val NEXT_STATE_BY_NUMBER_HREF = "$NEXT_STATES_HREF/{$NEXT_STATE_PARAM}"

    val STATE_BY_NUMBER_HREF_TEMPLATE = UriTemplate(STATE_BY_NUMBER_HREF)
    val STATES_HREF_TEMPLATE = UriTemplate(STATES_HREF)
    val NEXT_STATES_HREF_TEMPLATE = UriTemplate(NEXT_STATES_HREF)
    val NEXT_STATE_BY_NUMBER_HREF_TEMPLATE = UriTemplate(NEXT_STATE_BY_NUMBER_HREF)

    fun getStatesUri(projectId: Int) = STATES_HREF_TEMPLATE.expand(projectId)
    fun getStateByNumberUri(projectId: Int, stateNumber: Int) = STATE_BY_NUMBER_HREF_TEMPLATE.expand(projectId, stateNumber)
    fun getNextStatesUri(projectId: Int, stateNumber: Int) = NEXT_STATES_HREF_TEMPLATE.expand(projectId, stateNumber)
    fun getNextStateByNumberUri(projectId: Int, stateNumber: Int, nextStateNumber: Int) =
        NEXT_STATE_BY_NUMBER_HREF_TEMPLATE.expand(projectId, stateNumber, nextStateNumber)
    
    // Comments
    const val COMMENTS_HREF = "$ISSUE_BY_NUMBER_HREF/comments"
    const val COMMENT_BY_NUMBER_HREF = "$COMMENTS_HREF/{$COMMENT_PARAM}"

    val COMMENTS_HREF_TEMPLATE = UriTemplate(COMMENTS_HREF)
    val COMMENT_BY_NUMBER_HREF_TEMPLATE = UriTemplate(COMMENT_BY_NUMBER_HREF)

    fun getCommentsUri(projectId: Int, issueNumber: Int) = COMMENTS_HREF_TEMPLATE.expand(projectId, issueNumber)
    fun getCommentByNumberUri(projectId: Int, issueNumber: Int, commentNumber: Int) =
        COMMENT_BY_NUMBER_HREF_TEMPLATE.expand(projectId, issueNumber, commentNumber)

    // Users
    const val USER_HREF = "$BASE_URI/user"
    const val USERS_HREF = "$BASE_URI/users"
    const val USER_BY_ID_HREF = "$USERS_HREF/{$USER_PARAM}"

    val USER_BY_ID_HREF_TEMPLATE = UriTemplate(USER_BY_ID_HREF)

    fun getUserByIdUri(userId: Int) = USER_BY_ID_HREF_TEMPLATE.expand(userId)
    
    // Helpers
    fun createSirenLinkListForPagination(uri: URI, page: Int, limit: Int, collectionSize: Int): List<SirenLink> {
        val toReturn = mutableListOf(
            SirenLink(listOf("self"), UriTemplate("${uri}$PAGE_QUERY").expand(page, limit)),
            SirenLink(listOf("page"), hrefTemplate = "${uri}$PAGE_TEMPLATE_QUERY")
        )

        if (page > 0 && collectionSize > 0)
            toReturn.add(
                SirenLink(
                    listOf("previous"),
                    UriTemplate("${uri}$PAGE_QUERY")
                        .expand(page - 1, limit)
                )
            )

        if (collectionSize > ((page + 1) * limit))
            toReturn.add(
                SirenLink(
                    listOf("next"),
                    UriTemplate("${uri}$PAGE_QUERY")
                        .expand(page + 1, limit)
                )
            )

        return toReturn
    }
}