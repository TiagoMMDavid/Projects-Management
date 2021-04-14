package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.LabelCreateOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelDeleteOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel

private const val GET_LABELS_QUERY = "SELECT name, project FROM LABEL WHERE project = :projectName"

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectName}")
class LabelsController : BaseController() {

    @GetMapping("labels")
    fun getAllLabels(
        @PathVariable projectName: String,
    ): LabelsOutputModel {
        /*
        val labels = jdbi.withHandle<List<LabelDao>, Exception> {
            it.createQuery(GET_LABELS_QUERY)
                .bind("projectName", projectName.urlDecode())
                .mapTo(LabelDao::class.java)
                .list()
        }

        return LabelsOutputModel(
            PROJECTS_HREF,
            labels.map {
                LabelOutputListModel(
                    name = it.name,
                )
            }
        )

         */
        TODO()
    }

    @PutMapping("labels")
    fun createLabel(
        @PathVariable projectName: String,
        @RequestBody input: LabelInputModel,
    ): LabelCreateOutputModel {
        TODO()
    }

    @DeleteMapping("labels/{labelName}")
    fun deleteLabel(
        @PathVariable projectName: String,
        @PathVariable labelName: String,
    ): LabelDeleteOutputModel {
        TODO()
    }

    @PutMapping("issues/{issueId}/labels")
    fun addLabelToIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: LabelInputModel,
    ): LabelCreateOutputModel {
        TODO()
    }

    @DeleteMapping("issues/{issueId}/labels/{labelName}")
    fun deleteLabelFromIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @PathVariable labelName: String,
    ): LabelDeleteOutputModel {
        TODO()
    }
}