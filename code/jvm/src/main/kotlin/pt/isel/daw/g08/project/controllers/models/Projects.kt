package pt.isel.daw.g08.project.controllers.models

data class ProjectsOutputModel(
    val projects: List<ProjectOutputModel>,
)

data class ProjectOutputModel(
    val name: String,
    val description: String,
)

data class ProjectCreateInputModel(
    val name: String,
    val description: String,
)

data class ProjectEditInputModel(
    val description: String,
)

data class ProjectCreateOutputModel(
    val status: String,             // Created or Modified
    val projectDetails: String,     // URL to project
)