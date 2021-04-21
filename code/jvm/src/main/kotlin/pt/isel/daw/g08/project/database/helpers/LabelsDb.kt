package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.Label

private const val GET_LABELS_BASE = "SELECT lid, name, project_id, project_name, author_id, author_name FROM V_LABEL"

private const val GET_LABELS_FROM_PROJECT_QUERY = "$GET_LABELS_BASE WHERE project_id = :pid ORDER BY lid"
private const val GET_LABELS_COUNT_FROM_PROJECT = "SELECT COUNT(lid) as count FROM LABEL WHERE project = :pid"
private const val GET_LABEL_QUERY = "$GET_LABELS_BASE WHERE lid = :lid"
private const val GET_LABEL_BY_NAME_QUERY = "$GET_LABELS_BASE WHERE name = :name AND project_id = :pid"

private const val GET_LABELS_FROM_ISSUE_QUERY = "$GET_LABELS_BASE WHERE lid IN (SELECT lid FROM ISSUE_LABEL WHERE iid = :iid) ORDER BY lid"
private const val GET_LABELS_COUNT_FROM_ISSUE = "SELECT COUNT(lid) as count FROM ISSUE_LABEL WHERE iid = :iid"

private const val CREATE_LABEL_QUERY = "INSERT INTO LABEL(name, project, author) VALUES(:name, :project, :author)"
private const val INSERT_LABEL_IN_ISSUE_QUERY = "INSERT INTO ISSUE_LABEL(iid, lid) VALUES(:iid, :lid)"

private const val DELETE_LABEL_QUERY = "DELETE FROM LABEL WHERE lid = :lid"

@Component
class LabelsDb(val jdbi: Jdbi) {
    fun getAllLabelsFromProject(page: Int, perPage: Int, projectId: Int) =
        jdbi.getList(GET_LABELS_FROM_PROJECT_QUERY, Label::class.java, page, perPage, mapOf("pid" to projectId))

    fun getLabelsCountFromProject(projectId: Int) = jdbi.getOne(GET_LABELS_COUNT_FROM_PROJECT, Int::class.java, mapOf("pid" to projectId))
    fun getLabelById(labelId: Int) = jdbi.getOne(GET_LABEL_QUERY, Label::class.java , mapOf("lid" to labelId))
    fun getLabelByName(labelName: String, projectId: Int) =
        jdbi.getOne(
            GET_LABEL_BY_NAME_QUERY, Label::class.java,
            mapOf(
                "name" to labelName,
                "pid" to projectId
            )
        )

    fun getAllLabelsFromIssue(page: Int, perPage: Int, issueId: Int) =
        jdbi.getList(GET_LABELS_FROM_ISSUE_QUERY, Label::class.java, page, perPage, mapOf("iid" to issueId))
    fun getLabelsCountFromIssue(issueId: Int) = jdbi.getOne(GET_LABELS_COUNT_FROM_ISSUE, Int::class.java, mapOf("iid" to issueId))

    fun createLabel(name: String, projectId: Int, userId: Int) =
        jdbi.insert(
            CREATE_LABEL_QUERY, Int::class.java,
            mapOf(
                "name" to name,
                "project" to projectId,
                "author" to userId
            )
        )

    fun addLabelToIssue(labelId: Int, issueId: Int) =
        jdbi.insert(
            INSERT_LABEL_IN_ISSUE_QUERY, Int::class.java,
            mapOf(
                "iid" to issueId,
                "lid" to labelId
            )
        )

    fun deleteLabel(labelId: Int) = jdbi.delete(DELETE_LABEL_QUERY, mapOf("lid" to labelId))
}