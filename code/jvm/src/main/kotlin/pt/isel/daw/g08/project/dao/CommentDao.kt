package pt.isel.daw.g08.project.dao

import java.sql.Timestamp

data class CommentDao(
    val cid: Int,
    val iid: Int,
    val text: String,
    val createDate: Timestamp,
)
