package pt.isel.daw.g08.project.database.dto

data class State(
    val sid: Int,
    val name: String,
    val is_start: Boolean,

    val project_id: Int,
    val project_name: String,

    val author_id: Int,
    val author_name: String,
)
