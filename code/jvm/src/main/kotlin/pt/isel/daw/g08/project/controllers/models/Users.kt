package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.user

class UserOutputModel(
    val id: Int,
    val name: String,
) : OutputModel() {

    override fun getSirenClasses() = listOf(user)
}

class UsersOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int
) : OutputModel() {

    override fun getSirenClasses() = listOf(user, collection)
}