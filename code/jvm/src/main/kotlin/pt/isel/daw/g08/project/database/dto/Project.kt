package pt.isel.daw.g08.project.database.dto

data class Project(
    val pid: Int,
    val name: String,
    val description: String,

    val author_id: Int,
    val author_name: String,
)
