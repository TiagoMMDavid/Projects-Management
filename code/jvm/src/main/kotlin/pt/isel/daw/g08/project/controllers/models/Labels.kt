package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.label

class LabelOutputModel(
    val id: Int,
    val name: String,
    val project: String,
    val author: String
) : OutputModel() {

    override fun getSirenClasses() = listOf(label)
}

class LabelsOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int,
) : OutputModel() {

    override fun getSirenClasses() = listOf(label, collection)
}

data class LabelInputModel(
    val name: String,
)