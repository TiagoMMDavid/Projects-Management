package pt.isel.daw.g08.project.database.dto

import java.time.OffsetDateTime

data class Issue(
    val iid: Int,
    val number: Int,
    val name: String,
    val description: String,
    val create_date: OffsetDateTime,
    val close_date: OffsetDateTime?,

    val state_id: Int,
    val state_name: String,
    val state_number: Int,

    val project_id: Int,
    val project_name: String,

    val author_id: Int,
    val author_name: String,
)