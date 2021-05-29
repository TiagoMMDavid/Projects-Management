package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.comment
import java.time.OffsetDateTime

class CommentOutputModel(
    val id: Int,
    val number: Int,
    val content: String,
    val createDate: OffsetDateTime,
    val issue: String,
    val issueNumber: Int,
    val project: String,
    val projectId: Int,
    val author: String,
    val authorId: Int
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

data class CommentInputModel(
    val content: String?,
)