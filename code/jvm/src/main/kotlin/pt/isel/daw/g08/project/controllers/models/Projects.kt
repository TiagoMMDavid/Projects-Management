package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.Siren
import pt.isel.daw.g08.project.responses.SirenClass.collection
import pt.isel.daw.g08.project.responses.SirenClass.project

class ProjectOutputModel(
    id: Int,
    name: String,
    description: String,
    selfUrl: String,
    labelsUrl: String,
    issuesUrl: String,
    statesUrl: String,
    projectsUrl: String,
) : Siren(selfUrl, project) {
    init {
        // TODO: Rel
        super
            .addLink(false, labelsUrl, "labels")
            .addLink(false, issuesUrl,"issues")
            .addLink(false, statesUrl,"states")
            .addLink(false, projectsUrl, "projects")
            .addProperty("id", id)
            .addProperty("name", name)
            .addProperty("description", description)
    }
}

class ProjectsOutputModel(
    selfUrl: String,
    totalProjects: Int,
    projects: List<ProjectOutputModel>,
) : Siren(selfUrl, project, collection) {

    init {
        projects.forEach {
            addEntity(it.getJsonProperties())
        }
        super
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