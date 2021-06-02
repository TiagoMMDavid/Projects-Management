package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Label

private const val GET_LABELS_BASE = "SELECT lid, number, name, project_id, project_name, author_id, author_name FROM V_LABEL"

private const val GET_LABELS_FROM_PROJECT_QUERY = "$GET_LABELS_BASE WHERE project_id = :pid ORDER BY number"
private const val GET_LABELS_COUNT_FROM_PROJECT = "SELECT COUNT(lid) as count FROM LABEL WHERE project = :pid"
private const val GET_LABEL_QUERY = "$GET_LABELS_BASE WHERE project_id = :projectId AND number = :labelNumber"
private const val GET_LABEL_BY_ID_QUERY = "$GET_LABELS_BASE WHERE lid = :lid"
private const val GET_LABEL_BY_NAME_QUERY = "$GET_LABELS_BASE WHERE name = :name AND project_id = :pid"

private const val GET_LABELS_FROM_ISSUE_QUERY =
    "$GET_LABELS_BASE WHERE lid IN " +
            "(SELECT lid FROM ISSUE_LABEL WHERE iid IN" +
            "(SELECT iid FROM ISSUE WHERE project = :projectId AND number = :issueNumber)) ORDER BY V_LABEL.number"

private const val GET_LABELS_COUNT_FROM_ISSUE =
    "SELECT COUNT(lid) as count FROM ISSUE_LABEL WHERE iid IN (SELECT iid FROM ISSUE WHERE project = :projectId AND number = :issueNumber)"

private const val CREATE_LABEL_QUERY = "INSERT INTO LABEL(name, project, author) VALUES(:name, :project, :author)"
private const val UPDATE_LABEL_START = "UPDATE LABEL SET"
private const val UPDATE_LABEL_END = "WHERE project = :projectId AND number = :labelNumber"

private const val INSERT_LABEL_IN_ISSUE_QUERY = "INSERT INTO ISSUE_LABEL(iid, lid) VALUES(:iid, :lid)"

private const val DELETE_LABEL_QUERY = "DELETE FROM LABEL WHERE project = :projectId AND number = :labelNumber"
private const val DELETE_LABEL_FROM_ISSUE_QUERY = "DELETE FROM ISSUE_LABEL WHERE iid = :iid AND lid = :lid"

@Component
class LabelsDb(
    val projectsDb: ProjectsDb,
    val issuesDb: IssuesDb,
    val jdbi: Jdbi
) {

    fun getAllLabelsFromProject(page: Int, perPage: Int, projectId: Int): List<Label> {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.getList(GET_LABELS_FROM_PROJECT_QUERY, Label::class.java, page, perPage, mapOf("pid" to projectId))
    }

    fun getLabelsCountFromProject(projectId: Int) = jdbi.getOne(GET_LABELS_COUNT_FROM_PROJECT, Int::class.java, mapOf("pid" to projectId))

    fun getLabelByNumber(projectId: Int, labelNumber: Int) =
        jdbi.getOne(GET_LABEL_QUERY, Label::class.java ,
            mapOf(
                "projectId" to projectId,
                "labelNumber" to labelNumber
            )
        )

    fun getLabelByName(projectId: Int, labelName: String) =
        jdbi.getOne(
            GET_LABEL_BY_NAME_QUERY, Label::class.java,
            mapOf(
                "name" to labelName,
                "pid" to projectId
            )
        )

    fun getAllLabelsFromIssue(page: Int, perPage: Int, projectId: Int, issueNumber: Int): List<Label> {
        issuesDb.getIssueByNumber(projectId, issueNumber) // Check if issue exists (will throw exception if not found)
        return jdbi.getList(
            GET_LABELS_FROM_ISSUE_QUERY, Label::class.java, page, perPage,
            mapOf(
                "projectId" to projectId,
                "issueNumber" to issueNumber
            )
        )
    }

    fun getLabelsCountFromIssue(projectId: Int, issueNumber: Int) =
        jdbi.getOne(GET_LABELS_COUNT_FROM_ISSUE, Int::class.java,
            mapOf(
                "projectId" to projectId,
                "issueNumber" to issueNumber
            )
        )

    fun createLabel(projectId: Int, userId: Int, name: String): Label {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.insertAndGet(
            CREATE_LABEL_QUERY, Int::class.java,
            GET_LABEL_BY_ID_QUERY, Label::class.java,
            mapOf(
                "name" to name,
                "project" to projectId,
                "author" to userId
            ),
            "lid"
        )
    }

    fun editLabel(projectId: Int, labelNumber: Int, name: String) =
        jdbi.update(
            UPDATE_LABEL_START,
            mapOf("name" to name),
            UPDATE_LABEL_END,
            mapOf(
                "projectId" to projectId,
                "labelNumber" to labelNumber,
            )
        )

    fun addLabelToIssue(projectId: Int, issueNumber: Int, labelNumber: Int) {
        val issueId = issuesDb.getIssueByNumber(projectId, issueNumber).iid
        val labelId = getLabelByNumber(projectId, labelNumber).lid

        jdbi.insert(
            INSERT_LABEL_IN_ISSUE_QUERY, mapOf(
                "iid" to issueId,
                "lid" to labelId
            )
        )
    }

    fun deleteLabel(projectId: Int, labelNumber: Int) =
        jdbi.delete(DELETE_LABEL_QUERY,
            mapOf(
                "projectId" to projectId,
                "labelNumber" to labelNumber
            )
        )

    fun deleteLabelFromIssue(projectId: Int, issueNumber: Int, labelNumber: Int) {
        val labelId = getLabelByNumber(projectId, labelNumber).lid
        val issueId = issuesDb.getIssueByNumber(projectId, issueNumber).iid

        jdbi.delete(
            DELETE_LABEL_FROM_ISSUE_QUERY,
            mapOf(
                "iid" to issueId,
                "lid" to labelId
            )
        )
    }
}