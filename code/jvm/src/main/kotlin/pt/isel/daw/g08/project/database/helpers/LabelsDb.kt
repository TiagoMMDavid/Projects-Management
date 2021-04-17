package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.LabelDao

private const val GET_LABELS_BASE = "SELECT lid, name, project_id, project_name, author_id, author_name FROM V_LABEL"

private const val GET_LABELS_FROM_PROJECT_QUERY = "$GET_LABELS_BASE WHERE project_id = :pid ORDER BY lid"
private const val GET_LABELS_COUNT_FROM_PROJECT = "SELECT COUNT(lid) as count FROM LABEL WHERE project = :pid"
private const val GET_LABEL_QUERY = "$GET_LABELS_BASE WHERE lid = :lid"

private const val GET_LABELS_FROM_ISSUE_QUERY = "$GET_LABELS_BASE WHERE lid IN (SELECT lid FROM ISSUE_LABEL WHERE iid = :iid) ORDER BY lid"
private const val GET_LABELS_COUNT_FROM_ISSUE = "SELECT COUNT(lid) as count FROM ISSUE_LABEL WHERE iid = :iid"

@Component
class LabelsDb : DatabaseHelper() {
    fun getAllLabelsFromProject(page: Int, perPage: Int, projectId: Int) =
        getList(GET_LABELS_FROM_PROJECT_QUERY, LabelDao::class.java, page, perPage, Pair("pid", projectId))

    fun getLabelsCountFromProject(projectId: Int) = getOne(GET_LABELS_COUNT_FROM_PROJECT, Int::class.java, Pair("pid", projectId))
    fun getLabelById(labelId: Int) = getOne(GET_LABEL_QUERY, LabelDao::class.java , Pair("lid", labelId))


    fun getAllLabelsFromIssue(page: Int, perPage: Int, issueId: Int) =
        getList(GET_LABELS_FROM_ISSUE_QUERY, LabelDao::class.java, page, perPage, Pair("iid", issueId))
    fun getLabelsCountFromIssue(issueId: Int) = getOne(GET_LABELS_COUNT_FROM_ISSUE, Int::class.java, Pair("iid", issueId))
}