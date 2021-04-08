package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.LabelDeleteOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelInputModel
import pt.isel.daw.g08.project.controllers.models.LabelCreateOutputModel
import pt.isel.daw.g08.project.controllers.models.LabelsOutputModel

@RestController
@RequestMapping("/api/projects/{projectName}")
class LabelsController(val jdbi: Jdbi) {

    @GetMapping("labels")
    fun getAllLabels(
        @PathVariable projectName: String,
    ): LabelsOutputModel {
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