package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.LabelDao

private const val GET_ALL_LABELS_QUERY = "SELECT lid, name, project_id, project_name, author_id, author_name FROM V_LABEL"

private const val GET_LABELS_FROM_PROJECT_QUERY = "$GET_ALL_LABELS_QUERY WHERE project_id = :pid"
private const val GET_LABELS_COUNT = "SELECT COUNT(lid) as count FROM LABEL WHERE project = :pid"
private const val GET_LABEL_QUERY = "$GET_ALL_LABELS_QUERY WHERE lid = :lid"

@Component
class LabelsDb : DatabaseHelper() {
    fun getAllLabelsFromProject(page: Int, perPage: Int, projectId: Int) =
        boundedGetList(page, perPage, GET_LABELS_FROM_PROJECT_QUERY, "pid", projectId, LabelDao::class.java)

    fun getLabelsCount(projectId: Int) = boundedGetOne("pid", projectId, GET_LABELS_COUNT, Int::class.java)
    fun getLabelById(labelId: Int) = boundedGetOne("lid", labelId, GET_LABEL_QUERY, LabelDao::class.java)
}