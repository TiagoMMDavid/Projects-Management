package pt.isel.daw.g08.project.dao

data class ProjectDao(
    val pid: Int,
    val name: String,
    val description: String,

    val author_id: Int,
    val author_name: String,
)
