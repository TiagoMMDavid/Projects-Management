package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable

data class LabelOutputModel(
    val name: String,
)

data class LabelsOutputModel(
    val projects: List<LabelOutputModel>,
)

data class LabelPutResponseModel(
    val status: String,             // Created or Modified
    val labelDetails: String,
)

data class LabelDeleteResponseModel(
    val status: String,             // Deleted
)

@RestController
@RequestMapping("/api/projects/{projectName}/labels")
class LabelsController {

    @GetMapping()
    fun getAllLabels(
            @PathVariable projectName: String,
    ): LabelsOutputModel {
        TODO()
    }

    @PutMapping("{labelName}")
    fun putLabel(
            @PathVariable projectName: String,
            @PathVariable labelName: String,
    ): LabelPutResponseModel {
        TODO()
    }

    @DeleteMapping("{labelName}")
    fun deleteLabel(
            @PathVariable projectName: String,
            @PathVariable labelName: String,
    ): LabelDeleteResponseModel {
        TODO()
    }
}