package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.Hal

class ProjectOutputModel(
    name: String,
    description: String,
    selfUrl: String,
    labelsUrl: String,
    issuesUrl: String,
    statesUrl: String,
    projectsUrl: String,
) : Hal(selfUrl) {
    init {
        super
            .addLink("labels", labelsUrl)
            .addLink("issues", issuesUrl)
            .addLink("states", statesUrl)
            .addLink("projects", projectsUrl)
            .addProperty("name", name)
            .addProperty("description", description)
    }
}

class ProjectsOutputModel(
    selfUrl: String,
    totalProjects: Int,
    projects: List<ProjectOutputModel>,
) : Hal(selfUrl) {

    init {
        super
            .addEmbedded("projects", projects)
            .addProperty("totalProjects", totalProjects)
    }
}

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