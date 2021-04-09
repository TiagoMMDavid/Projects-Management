package pt.isel.daw.g08.project.controllers.models

import java.sql.Timestamp

data class CommentOutputModel(
    val comment: String,
    val createdAt: Timestamp,
)

data class CommentsOutputModel(
    val comments: List<CommentOutputModel>,
)

data class CommentCreateOutputModel(
    val comment: String,
)

data class CommentCreateInputModel(
    val comment: String,
)

data class CommentEditOutputModel(
    val comment: String,
)

data class CommentEditInputModel(
    val comment: String,
)

data class CommentDeleteOutputModel(
    val comment: String,
)