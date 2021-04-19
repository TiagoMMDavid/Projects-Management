package pt.isel.daw.g08.project.database.dao

import java.time.OffsetDateTime

data class CommentDao(
    val cid: Int,
    val text: String,
    val create_date: OffsetDateTime,

    val issue_id: Int,
    val issue_name: String,

    val author_id: Int,
    val author_name: String,
)
