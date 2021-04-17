package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.project

class ProjectOutputModel(
    val id: Int,
    val name: String,
    val description: String,
    val author: String,
) : OutputModel() {

    override fun getSirenClasses() = listOf(project)
}

class ProjectsOutputModel(
    val collectionSize: Int,
    val pageIndex: Int,
    val pageSize: Int
) : OutputModel() {

    override fun getSirenClasses() = listOf(project, collection)
}

data class ProjectCreateInputModel(
    val name: String,
    val description: String,
)

data class ProjectEditInputModel(
    val name: String,
    val description: String,
)