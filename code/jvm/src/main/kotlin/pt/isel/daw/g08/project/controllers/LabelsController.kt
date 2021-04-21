package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.ISSUE_PARAM
import pt.isel.daw.g08.project.Routes.LABELS_HREF
import pt.isel.daw.g08.project.Routes.LABELS_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.LABEL_BY_NUMBER_OF_ISSUE_HREF
import pt.isel.daw.g08.project.Routes.LABEL_PARAM
import pt.isel.daw.g08.project.Routes.PROJECT_PARAM
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getIssueByNumberUri
import pt.isel.daw.g08.project.Routes.getLabelByNumberOfIssueUri
import pt.isel.daw.g08.project.Routes.getLabelByNumberUri
import pt.isel.daw.g08.project.Routes.getLabelsOfIssueUri
import pt.isel.daw.g08.project.Routes.getLabelsUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.IssuesDb
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
class LabelsController(val db: LabelsDb, val issuesDb: IssuesDb) {

    @RequiresAuth
    @GetMapping(LABELS_HREF)
    fun getAllLabels(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
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
                    number = it.number,
                    name = it.name,
                    project = it.project_name,
                    author = it.author_name,
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getLabelByNumberUri(projectId, it.number).includeHost()),
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
    @GetMapping(LABEL_BY_NUMBER_HREF)
    fun getLabel(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = LABEL_PARAM) labelNumber: Int,
    ): ResponseEntity<Response> {
        val label = db.getLabelByNumber(projectId, labelNumber)
        val labelModel = LabelOutputModel(
            id = label.lid,
            number = label.number,
            name = label.name,
            project = label.project_name,
            author = label.author_name,
        )

        val labelByIdUri = getLabelByNumberUri(projectId, labelNumber).includeHost()

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
                        SirenActionField(name = "labelNumber", type = SirenFieldType.hidden, value = labelModel.number),
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
                        SirenActionField(name = "labelNumber", type = SirenFieldType.hidden, value = labelModel.number),
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
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        input: LabelInputModel,
        user: User
    ): ResponseEntity<Response> {
        if (input.name == null) throw InvalidInputException("Missing name")

        val label = db.createLabel(input.name, projectId, user.uid)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getLabelByNumberUri(projectId, label.number).includeHost().toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(LABEL_BY_NUMBER_HREF)
    fun deleteLabel(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = LABEL_PARAM) labelNumber: Int,
    ): ResponseEntity<Response> {
        db.deleteLabel(projectId, labelNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }

    @RequiresAuth
    @GetMapping(LABELS_OF_ISSUE_HREF)
    fun getLabelsFromIssue(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        pagination: Pagination
    ) : ResponseEntity<Response> {
        val labels = db.getAllLabelsFromIssue(pagination.page, pagination.limit, projectId, issueNumber)
        val collectionSize = db.getLabelsCountFromIssue(projectId, issueNumber)
        val labelsModel = LabelsOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = labels.size
        )

        val issueLabelsUri = getLabelsOfIssueUri(projectId, issueNumber).includeHost()

        return labelsModel.toSirenObject(
            entities = labels.map {
                LabelOutputModel(
                    id = it.lid,
                    number = it.number,
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
                            href = getLabelByNumberOfIssueUri(projectId, issueNumber, it.number).includeHost(),
                            fields = listOf(
                                SirenActionField(name = "projectId", type = SirenFieldType.hidden, value = it.project_id),
                                SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueNumber),
                                SirenActionField(name = "labelNumber", type = SirenFieldType.hidden, value = it.number),
                            )
                        )
                    ),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getLabelByNumberOfIssueUri(projectId, issueNumber, it.number).includeHost()),
                        SirenLink(rel = listOf("issue"), href = getIssueByNumberUri(projectId, issueNumber).includeHost()),
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
                        SirenActionField(name = "issueNumber", type = SirenFieldType.hidden, value = issueNumber),
                        SirenActionField(name = "labelName", type = SirenFieldType.text)
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
                SirenLink(rel = listOf("issue"), href = getIssueByNumberUri(projectId, issueNumber).includeHost()),
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PutMapping(LABELS_OF_ISSUE_HREF)
    fun addLabelToIssue(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        input: LabelInputModel,
    ): ResponseEntity<Response> {
        if (input.name == null) throw InvalidInputException("Missing name")

        db.addLabelToIssue(projectId, issueNumber, input.name)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getLabelsOfIssueUri(projectId, issueNumber).includeHost().toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(LABEL_BY_NUMBER_OF_ISSUE_HREF)
    fun deleteLabelFromIssue(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = ISSUE_PARAM) issueNumber: Int,
        @PathVariable(name = LABEL_PARAM) labelNumber: Int,
    ): ResponseEntity<Response> {
        db.deleteLabelFromIssue(projectId, issueNumber, labelNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }
}