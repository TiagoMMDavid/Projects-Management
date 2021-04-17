package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.comment
import java.sql.Timestamp

class CommentOutputModel(
    val id: Int,
    val content: String,
    val createDate: Timestamp,
    val issue: String,
    val author: String,
) : OutputModel() {
    override fun getSirenClasses() = listOf(comment)
}

class CommentsOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int,
) : OutputModel() {
    override fun getSirenClasses() = listOf(comment, collection)
}

data class CommentCreateInputModel(
    val comment: String,
)

data class CommentEditInputModel(
    val comment: String,
)