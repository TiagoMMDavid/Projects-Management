package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenClass
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.state

class StateOutputModel(
    val id: Int,
    val name: String,
    val isStartState: Boolean,
    val projectName: String,
    val author: String,
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

data class StateInputModel(
    val isStartState: Boolean?,
    val nextStates: List<String>?,
)
