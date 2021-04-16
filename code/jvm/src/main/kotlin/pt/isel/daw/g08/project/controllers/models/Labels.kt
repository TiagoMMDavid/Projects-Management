package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.label

class LabelOutputModel(
    id: Int,
    name: String,
    projectName: String,
    authorName: String,
    selfUrl: String,
    authorUrl: String,
    projectUrl: String,
    labelsUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, label) {
    init {
        super
            .addProperty("id", id)
            .addProperty("name", name)
            .addProperty("project", projectName)
            .addProperty("author", authorName)
            .addLink(projectUrl, false, "project")
            .addLink(authorUrl, false, "author")
            .addLink(labelsUrl, false, "labels")

        if (isCollectionItem) super.addRelation("item")
    }
}

class LabelsOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    labels: List<LabelOutputModel>,
) : Siren(selfUrl, label, collection) {
    init {
        labels.forEach {
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

data class LabelInputModel(
    val name: String,
)