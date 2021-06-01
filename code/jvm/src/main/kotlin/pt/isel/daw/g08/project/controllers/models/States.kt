package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.state

class StateOutputModel(
    val id: Int,
    val number: Int,
    val name: String,
    val isStartState: Boolean,
    val project: String,
    val projectId: Int,
    val author: String,
    val authorId: Int
) : OutputModel() {
    override fun getSirenClasses(): List<SirenClass> = listOf(state)

}

class StatesOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int,
) : OutputModel() {
    override fun getSirenClasses(): List<SirenClass> = listOf(state, collection)

}

class NextStatesOutputModel(
    val collectionSize: Int
) : OutputModel() {
    override fun getSirenClasses(): List<SirenClass> = listOf(state, collection)

}

data class StateInputModel(
    val name: String?,
    val isStart: Boolean?,
)

data class NextStateInputModel(
    val state: String?
)
