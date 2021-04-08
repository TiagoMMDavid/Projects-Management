package pt.isel.daw.g08.project.controllers.models

import java.util.*

data class IssueOutputModel(
    val id: Int,
    val name: String,
    val description: String,
    val project: String,
    val state: String,
    val creationDate: Date,
    val closeDate: Date?,
)

data class IssuesOutputModel(
    val issues: List<IssueOutputModel>,
)

data class IssueCreateInputModel(
    val name: String,
    val description: String,
    val closeDate: Date?,
)

data class IssueEditInputModel(
    val name: String?,
    val description: String?,
    val state: String?,
    val closeDate: Date?,
)

data class IssueCreateResponseModel(
    val createdId: String,
    val issueDetails: String,
)

data class IssueEditResponseModel(
    val status: String,             // Modified
    val issueDetails: String,
)