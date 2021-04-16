package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel
import pt.isel.daw.g08.project.database.helpers.LabelsDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}")
class LabelsController(val db: LabelsDb) : BaseController() {

    @GetMapping("labels")
    fun getAllLabels(
        @PathVariable projectId: Int,
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val labels = db.getAllLabelsFromProject(page, count, projectId)
        val collectionSize = db.getLabelsCount(projectId)

        return createResponseEntity(
            LabelsOutputModel(
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = labels.size,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels{?pageIndex,pageSize}",
                labels = labels.map {
                    LabelOutputModel(
                        id = it.lid,
                        name = it.name,
                        projectName = it.project_name,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels/${it.lid}",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                        labelsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @GetMapping("labels/{labelId}")
    fun getLabel(
        @PathVariable projectId: Int,
        @PathVariable labelId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val label = db.getLabelById(labelId)

        return createResponseEntity(
            LabelOutputModel(
                id = label.lid,
                name = label.name,
                projectName = label.project_name,
                authorName = label.author_name,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels/${label.lid}",
                authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${label.author_id}",
                projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                labelsUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/labels",
            ),
            200
        )
    }

    @PutMapping("labels")
    fun createLabel(
        @PathVariable projectId: Int,
        @RequestBody input: LabelInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping("labels/{labelName}")
    fun deleteLabel(
        @PathVariable projectName: String,
        @PathVariable labelName: String,
    ): ResponseEntity<Response> {
        TODO()
    }

    @PutMapping("issues/{issueId}/labels")
    fun addLabelToIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: LabelInputModel,
    ): ResponseEntity<Response> {
        TODO()
    }

    @DeleteMapping("issues/{issueId}/labels/{labelName}")
    fun deleteLabelFromIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @PathVariable labelName: String,
    ): ResponseEntity<Response> {
        TODO()
    }
}