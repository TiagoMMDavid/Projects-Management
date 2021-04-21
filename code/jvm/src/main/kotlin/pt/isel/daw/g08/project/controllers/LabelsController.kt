package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.LABELS_HREF
import pt.isel.daw.g08.project.Routes.LABELS_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_ID_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getIssueByIdUri
import pt.isel.daw.g08.project.Routes.getLabelByIdOfIssue
import pt.isel.daw.g08.project.Routes.getLabelByIdUri
import pt.isel.daw.g08.project.Routes.getLabelsOfIssueUri
import pt.isel.daw.g08.project.Routes.getLabelsUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.LabelsDb
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity

@RestController
class LabelsController(val db: LabelsDb) {

    @RequiresAuth
    @GetMapping(LABELS_HREF)
    fun getAllLabels(
        @PathVariable projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val labels = db.getAllLabelsFromProject(pagination.page, pagination.limit, projectId)
        val collectionSize = db.getLabelsCountFromProject(projectId)
        val labelsModel = LabelsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = labels.size
        )

        val labelsUri = getLabelsUri(projectId).includeHost()

        return labelsModel.toSirenObject(
            entities = labels.map {
                LabelOutputModel(
                    id = it.lid,
                    name = it.name,
                    project = it.project_name,
                    author = it.author_name,
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getLabelByIdUri(projectId, it.lid).includeHost()),
                        SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                        SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id).includeHost()),
                        SirenLink(rel = listOf("labels"), href = labelsUri)
                    ),
                )
            },
            actions = listOf(
                SirenAction(
                    name = "create-label",
                    title = "Create Label",
                    method = HttpMethod.PUT,
                    href = labelsUri,
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "name", type = SirenFieldType.text)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                labelsUri,
                pagination.page,
                labelsModel.pageSize,
                pagination.limit,
                collectionSize
            ) + listOf(SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()))
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(LABEL_BY_ID_HREF)
    fun getLabel(
        @PathVariable projectId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        val label = db.getLabelById(projectId)
        val labelModel = LabelOutputModel(
            id = label.lid,
            name = label.name,
            project = label.project_name,
            author = label.author_name,
        )

        val labelByIdUri = getLabelByIdUri(projectId, labelId).includeHost()

        return labelModel.toSirenObject(
            actions = listOf(
                SirenAction(
                    name = "edit-label",
                    title = "Edit Label",
                    method = HttpMethod.PUT,
                    href = labelByIdUri,
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = labelModel.id),
                        SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = labelModel.id),
                        SirenActionField(name = "name", type = SirenFieldType.text)
                    )
                ),
                SirenAction(
                    name = "delete-label",
                    title = "Delete Label",
                    method = HttpMethod.DELETE,
                    href = labelByIdUri,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = label.project_id),
                        SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = labelModel.id),
                    )
                )
            ),
            links = listOf(
                SirenLink(rel = listOf("self"), href = labelByIdUri),
                SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                SirenLink(rel = listOf("author"), href = getUserByIdUri(label.author_id).includeHost()),
                SirenLink(rel = listOf("labels"), href = getLabelsUri(projectId).includeHost())
            ),
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PutMapping(LABELS_HREF)
    fun createLabel(
        @PathVariable projectId: Int,
        input: LabelInputModel,
        user: User
    ): ResponseEntity<Response> {
        if (input.name == null) throw InvalidInputException("Missing name")

        val labelId = db.createLabel(input.name, projectId, user.uid)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getLabelByIdUri(projectId, labelId).includeHost().toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(LABEL_BY_ID_HREF)
    fun deleteLabel(
        @PathVariable projectId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }

    @RequiresAuth
    @GetMapping(LABELS_OF_ISSUE_HREF)
    fun getLabelsFromIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        pagination: Pagination
    ) : ResponseEntity<Response> {
        val labels = db.getAllLabelsFromIssue(pagination.page, pagination.limit, issueId)
        val collectionSize = db.getLabelsCountFromIssue(issueId)
        val labelsModel = LabelsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = labels.size
        )

        val issueLabelsUri = getLabelsOfIssueUri(projectId, issueId).includeHost()

        return labelsModel.toSirenObject(
            entities = labels.map {
                LabelOutputModel(
                    id = it.lid,
                    name = it.name,
                    project = it.project_name,
                    author = it.author_name,
                ).toSirenObject(
                    rel = listOf("item"),
                    actions = listOf(
                        SirenAction(
                            name = "delete-label-from-issue",
                            title = "Delete a Label from an issue",
                            method = HttpMethod.DELETE,
                            href = getLabelByIdOfIssue(projectId, issueId, it.lid).includeHost(),
                            fields = listOf(
                                SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = it.project_id),
                                SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                                SirenActionField(name = "labelId", type = SirenFieldType.hidden, value = it.lid),
                            )
                        )
                    ),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getLabelByIdOfIssue(projectId, issueId, it.lid).includeHost()),
                        SirenLink(rel = listOf("issue"), href = getIssueByIdUri(projectId, issueId).includeHost()),
                        SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                        SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id).includeHost()),
                        SirenLink(rel = listOf("labels"), href = issueLabelsUri)
                    ),
                )
            },
            actions = listOf(
                SirenAction(
                    name = "add-label-to-issue",
                    title = "Add a Label to an issue",
                    method = HttpMethod.PUT,
                    href = issueLabelsUri,
                    type = INPUT_CONTENT_TYPE,
                    fields = listOf(
                        SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = projectId),
                        SirenActionField(name = "issueId", type = SirenFieldType.hidden, value = issueId),
                        SirenActionField(name = "labelId", type = SirenFieldType.number)
                    )
                )
            ),
            links = createSirenLinkListForPagination(
                issueLabelsUri,
                pagination.page,
                labelsModel.pageSize,
                pagination.limit,
                collectionSize
            ) + listOf(
                SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                SirenLink(rel = listOf("issue"), href = getIssueByIdUri(projectId, issueId).includeHost()),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PutMapping(LABELS_OF_ISSUE_HREF)
    fun addLabelToIssue(
        @PathVariable projectId: String,
        @PathVariable issueId: Int,
        @RequestBody input: LabelInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @RequiresAuth
    @DeleteMapping(LABEL_BY_ID_OF_ISSUE_HREF)
    fun deleteLabelFromIssue(
        @PathVariable projectId: Int,
        @PathVariable issueId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        TODO()
    }
}