package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.issue
import java.sql.Timestamp
import java.util.*

class IssueOutputModel(
    id: Int,
    name: String,
    description: String,
    createDate: Timestamp,
    closeDate: Timestamp?,
    stateName: String,
    projectName: String,
    authorName: String,
    selfUrl: String,
    stateUrl: String,
    commentsUrl: String,
    authorUrl: String,
    projectUrl: String,
    issuesUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, issue) {
    init {
        super
            .addProperty("id", id)
            .addProperty("name", name)
            .addProperty("description", description)
            .addProperty("createDate", createDate)
            .addProperty("closeDate", closeDate)
            .addProperty("state", stateName)
            .addProperty("project", projectName)
            .addProperty("author", authorName)
            .addLink(stateUrl, false, "state")
            .addLink(commentsUrl, false, "comments")
            .addLink(projectUrl, false, "project")
            .addLink(authorUrl, false, "author")
            .addLink(issuesUrl, false, "issues")

        if (isCollectionItem) super.addRelation("item")
    }
}

class IssuesOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    issues: List<IssueOutputModel>,
) : Siren(selfUrl, issue, collection) {
    init {
        issues.forEach {
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