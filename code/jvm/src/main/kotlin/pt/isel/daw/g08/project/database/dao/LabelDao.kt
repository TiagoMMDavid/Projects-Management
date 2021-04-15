package pt.isel.daw.g08.project.database.dao

data class LabelDao(
    val lid: Int,
    val name: String,

    val project_id: Int,
    val project_name: String,

    val author_id: Int,
    val author_name: String,
)
