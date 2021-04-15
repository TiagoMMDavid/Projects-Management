package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass.*

class UserOutputModel(
    id: Int,
    name: String,
    selfUrl: String,
    usersUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, user) {
    init {
        super
            .addProperty("id", id)
            .addProperty("name", name)
            .addLink(usersUrl, false, "users")

        if (isCollectionItem) super.addRelation("item")
    }
}

class UsersOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    users: List<UserOutputModel>,
) : Siren(selfUrl, user, collection) {
    init {
        users.forEach {
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