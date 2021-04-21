package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.issue
import java.time.OffsetDateTime

class IssueOutputModel(
    val id: Int,
    val number: Int,
    val name: String,
    val description: String,
    val createDate: OffsetDateTime,
    val closeDate: OffsetDateTime?,
    val state: String,
    val stateNumber: Int,
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
    val closeDate: OffsetDateTime?,
)

data class IssueEditInputModel(
    val name: String?,
    val description: String?,
    val state: String?,
    val closeDate: OffsetDateTime?,
)