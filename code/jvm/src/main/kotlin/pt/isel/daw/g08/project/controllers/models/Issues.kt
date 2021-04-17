package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.issue
import java.sql.Timestamp

class IssueOutputModel(
    val id: Int,
    val name: String,
    val description: String,
    val createDate: Timestamp,
    val closeDate: Timestamp?,
    val state: String,
    val project: String,
    val author: String,
) : OutputModel() {
    override fun getSirenClasses() = listOf(issue)
}

class IssuesOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int,
) : OutputModel() {
    override fun getSirenClasses() = listOf(issue, collection)
}

data class IssueCreateInputModel(
    val name: String,
    val description: String,
    val closeDate: Timestamp?,
)

data class IssueEditInputModel(
    val name: String?,
    val description: String?,
    val state: String?,
    val closeDate: Timestamp?,
)