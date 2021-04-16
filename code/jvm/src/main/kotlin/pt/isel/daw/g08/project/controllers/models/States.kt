package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.state

class StateOutputModel(
    id: Int,
    name: String,
    isStartState: Boolean,
    projectName: String,
    authorName: String,
    selfUrl: String,
    authorUrl: String,
    projectUrl: String,
    nextStatesUrl: String,
    statesUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, state) {
    init {
        super
            .addProperty("id", id)
            .addProperty("name", name)
            .addProperty("isStartState", isStartState)
            .addProperty("project", projectName)
            .addProperty("author", authorName)
            .addLink(nextStatesUrl, false, "nextStates")
            .addLink(projectUrl, false, "project")
            .addLink(authorUrl, false, "author")
            .addLink(statesUrl, false, "states")

        if (isCollectionItem) super.addRelation("item")
    }
}

class StatesOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    states: List<StateOutputModel>,
) : Siren(selfUrl, state, collection) {
    init {
        states.forEach {
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

data class StateInputModel(
    val isStartState: Boolean?,
    val nextStates: List<String>?,
)
