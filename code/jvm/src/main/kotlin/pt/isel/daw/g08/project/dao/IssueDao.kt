package pt.isel.daw.g08.project.dao

import java.sql.Timestamp

data class IssueDao(
    val iid: Int,
    val name: String,
    val description: String,
    val create_date: Timestamp,
    val close_date: Timestamp?,

    val state_id: Int,
    val state_name: String,

    val project_id: Int,
    val project_name: String,

    val author_id: Int,
    val author_name: String,
)