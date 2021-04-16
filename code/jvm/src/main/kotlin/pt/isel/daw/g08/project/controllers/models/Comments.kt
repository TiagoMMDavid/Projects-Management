package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.comment
import java.sql.Timestamp

class CommentOutputModel(
    id: Int,
    text: String,
    createDate: Timestamp,
    issueName: String,
    authorName: String,
    selfUrl: String,
    issueUrl: String,
    authorUrl: String,
    commentsUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, comment) {
    init {
        super
            .addProperty("id", id)
            .addProperty("text", text)
            .addProperty("createDate", createDate)
            .addProperty("issue", issueName)
            .addProperty("author", authorName)
            .addLink(issueUrl, false, "issue")
            .addLink(authorUrl, false, "author")
            .addLink(commentsUrl, false, "comments")

        if (isCollectionItem) super.addRelation("item")
    }
}

class CommentsOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    comments: List<CommentOutputModel>,
) : Siren(selfUrl, comment, collection) {
    init {
        comments.forEach {
            addEntity(it.getJsonProperties())
        }
        super
            .addProperty("collectionSize", collectionSize)
            .addProperty("pageIndex", pageIndex)
            .addProperty("pageSize", pageSize)
            .addLink(templateUrl, true, "page")

        if (pageIndex > 0) {
            super.addLink(prevUrl, false, "previous")
        }

        if (collectionSize != ((pageIndex + 1) * pageSize)) {
            super.addLink(nextUrl, false, "next")
        }
    }
}

data class CommentCreateInputModel(
    val comment: String,
)

data class CommentEditInputModel(
    val comment: String,
)