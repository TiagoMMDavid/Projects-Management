package pt.isel.daw.g08.project.controllers.models

import org.springframework.http.HttpMethod
import pt.isel.daw.g08.project.controllers.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenClass.collection
import pt.isel.daw.g08.project.responses.siren.SirenClass.project
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text

class ProjectOutputModel(
    id: Int,
    name: String,
    description: String,
    authorName: String,
    selfUrl: String,
    labelsUrl: String,
    issuesUrl: String,
    statesUrl: String,
    authorUrl: String,
    projectsUrl: String,
    isCollectionItem: Boolean = false
) : Siren(selfUrl, project) {
    init {
        super
            .addProperty("id", id)
            .addProperty("name", name)
            .addProperty("description", description)
            .addProperty("author", authorName)
            .addLink(labelsUrl, false, "labels")
            .addLink(issuesUrl, false, "issues")
            .addLink(statesUrl, false, "states")
            .addLink(authorUrl, false, "author")
            .addLink(projectsUrl, false, "projects")

        if (isCollectionItem) {
            super.addRelation("item")
        }
        else {
            val editProjectAction = SirenAction(
                name = "edit-project",
                title = "Edit Project",
                method = HttpMethod.PUT,
                href = selfUrl,
                type = INPUT_CONTENT_TYPE
            )
            editProjectAction
                .addField("projectId", hidden, id)
                .addField("name", text)
                .addField("description", text)

            val deleteProjectAction = SirenAction(
                name = "delete-project",
                title = "Delete Project",
                method = HttpMethod.DELETE,
                href = selfUrl
            )
            deleteProjectAction
                .addField("projectId", hidden, id)

            super
                .addAction(editProjectAction)
                .addAction(deleteProjectAction)
        }
    }
}

class ProjectsOutputModel(
    collectionSize: Int,
    pageIndex: Int,
    pageSize: Int,
    selfUrl: String,
    templateUrl: String,
    nextUrl: String,
    prevUrl: String,
    projects: List<ProjectOutputModel>,
) : Siren(selfUrl, project, collection) {

    init {
        projects.forEach {
            addEntity(it.getJsonProperties())
        }
        super
            .addProperty("collectionSize", collectionSize)
            .addProperty("pageIndex", pageIndex)
            .addProperty("pageSize", pageSize)
            .addLink(templateUrl, true, "page")

        if (pageIndex > 0) {
            super.addLink(prevUrl, false, "previous")
        }

        if (collectionSize != ((pageIndex + 1) * pageSize)) {
            super.addLink(nextUrl, false, "next")
        }

        val createProjectAction = SirenAction(
            name = "create-project",
            title = "Create Project",
            method = HttpMethod.PUT,
            href = selfUrl.substringBefore('?'),
            type = INPUT_CONTENT_TYPE
        )
        createProjectAction
            .addField("name", text)
            .addField("description", text)

        super.addAction(createProjectAction)
    }
}

data class ProjectCreateInputModel(
    val name: String,
    val description: String,
)

data class ProjectEditInputModel(
    val name: String,
    val description: String,
)