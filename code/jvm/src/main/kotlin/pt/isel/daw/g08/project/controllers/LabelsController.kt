package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel
import pt.isel.daw.g08.project.database.helpers.LabelsDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}")
class LabelsController(val db: LabelsDb) : BaseController() {

    @GetMapping("labels")
    fun getAllLabels(
        @PathVariable projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val labelsDao = db.getAllLabelsFromProject(pagination.page, pagination.limit, projectId)
        val collectionSize = db.getLabelsCountFromProject(projectId)
        val labels = LabelsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = labelsDao.size
        )
        val labelsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels"

        return createResponseEntity(
            labels.toSirenObject(
                entities = labelsDao.map { labelDao ->
                    LabelOutputModel(
                        id = labelDao.lid,
                        name = labelDao.name,
                        project = labelDao.project_name,
                        author = labelDao.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = URI("${labelsUri}/${labelDao.lid}")),
                            SirenLink(rel = listOf("project"), href = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${labelDao.project_id}")),
                            SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${labelDao.author_id}")),
                            SirenLink(rel = listOf("labels"), href = URI(labelsUri))
                        ),
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-label",
                        title = "Create Label",
                        method = HttpMethod.PUT,
                        href = URI(labelsUri),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "name", type = SirenFieldType.text)
                        )
                    )
                ),
                links = createUriListForPagination(labelsUri, pagination.page, labels.pageSize, pagination.limit, collectionSize)
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("labels/{labelId}")
    fun getLabel(
        @PathVariable projectId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val labelDao = db.getLabelById(projectId)
        val label = LabelOutputModel(
            id = labelDao.lid,
            name = labelDao.name,
            project = labelDao.project_name,
            author = labelDao.author_name,
        )
        val selfUri = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/labels/${label.id}")
        val labelsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels"

        return createResponseEntity(
            label.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-label",
                        title = "Edit Label",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = label.id),
                            SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = label.id),
                            SirenActionField(name = "name", type = SirenFieldType.text)
                        )
                    ),
                    SirenAction(
                        name = "delete-label",
                        title = "Delete Label",
                        method = HttpMethod.DELETE,
                        href = selfUri,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = labelDao.project_id),
                            SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = label.id),
                        )
                    )
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = URI("${labelsUri}/${labelDao.lid}")),
                    SirenLink(rel = listOf("project"), href = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${labelDao.project_id}")),
                    SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${labelDao.author_id}")),
                    SirenLink(rel = listOf("labels"), href = URI(labelsUri))
                ),
            ),
            HttpStatus.OK
        )
    }

    @PutMapping("labels")
    fun createLabel(
        @PathVariable projectId: Int,
        @RequestBody input: LabelInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping("labels/{labelId}")
    fun deleteLabel(
        @PathVariable projectId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }

    @GetMapping("issues/{issueId}/labels")
    fun getLabelsFromIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        pagination: Pagination
    ) : ResponseEntity<Response> {
        val labelsDao = db.getAllLabelsFromIssue(pagination.page, pagination.limit, issueId)
        val collectionSize = db.getLabelsCountFromIssue(issueId)
        val labels = LabelsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = labelsDao.size
        )
        val labelsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels"
        val issueLabelsUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/issues/${issueId}/labels"

        return createResponseEntity(
            labels.toSirenObject(
                entities = labelsDao.map { labelDao ->
                    LabelOutputModel(
                        id = labelDao.lid,
                        name = labelDao.name,
                        project = labelDao.project_name,
                        author = labelDao.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        actions = listOf(
                            SirenAction(
                                name = "delete-label-from-issue",
                                title = "Delete a Label from an issue",
                                method = HttpMethod.DELETE,
                                href = URI("${issueLabelsUri}/${labelDao.lid}"),
                                fields = listOf(
                                    SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = labelDao.project_id),
                                    SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                                    SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = labelDao.lid),
                                )
                            )
                        ),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = URI("${labelsUri}/${labelDao.lid}")),
                            SirenLink(rel = listOf("project"), href = URI("${env.getBaseUrl()}/${PROJECTS_HREF}/${labelDao.project_id}")),
                            SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${labelDao.author_id}")),
                            SirenLink(rel = listOf("labels"), href = URI(labelsUri))
                        ),
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "add-label-to-issue",
                        title = "Add a Label to an issue",
                        method = HttpMethod.PUT,
                        href = URI(issueLabelsUri),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                            SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                            SirenActionField(name = "labelId", type = SirenFieldType.number)
                        )
                    )
                ),
                links = createUriListForPagination(issueLabelsUri, pagination.page, labels.pageSize, pagination.limit, collectionSize)
            ),
            HttpStatus.OK
        )
    }

    @PutMapping("issues/{issueId}/labels")
    fun addLabelToIssue(
        @PathVariable projectId: String,
        @PathVariable issueId: Int,
        @RequestBody input: LabelInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping("issues/{issueId}/labels/{labelId}")
    fun deleteLabelFromIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }
}