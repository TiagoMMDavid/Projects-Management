package pt.isel.daw.g08.project.database.dao

import java.time.OffsetDateTime

data class IssueDao(
    val iid: Int,
    val name: String,
    val description: String,
    val create_date: OffsetDateTime,
    val close_date: OffsetDateTime?,

    val state_id: Int,
    val state_name: String,

    val project_id: Int,
    val project_name: String,

    val author_id: Int,
    val author_name: String,
)