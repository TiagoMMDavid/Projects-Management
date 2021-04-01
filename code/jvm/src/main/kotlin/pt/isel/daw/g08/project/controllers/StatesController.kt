package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

data class StateOutputModel(
    val name: String,
    val isStartState: Boolean,
    val nextStates: List<String>,
)

data class StatesOutputModel(
    val states: List<StateOutputModel>,
)

data class StateInputModel(
    val isStartState: Boolean?,
    val nextStates: List<String>?,
)

data class StatePutResponseModel(
    val status: String,             // Created or Modified
    val stateDetails: String,
)

data class StateDeleteResponseModel(
    val status: String,             // Deleted
)

@RestController
@RequestMapping("/api/projects/{projectName}/states")
class StatesController {

    @GetMapping()
    fun getAllStates(
            @PathVariable projectName: String,
    ): StatesOutputModel {
        TODO()
    }

    @GetMapping("{stateName}")
    fun getState(
            @PathVariable projectName: String,
            @PathVariable stateName: String,
    ): StateOutputModel {
        TODO()
    }

    @PutMapping("{stateName}")
    fun putState(
            @PathVariable projectName: String,
            @PathVariable stateName: String,
            @RequestBody input: StateInputModel,
    ): StatePutResponseModel {
        TODO()
    }

    @DeleteMapping("{stateName}")
    fun deleteState(
            @PathVariable projectName: String,
            @PathVariable stateName: String,
    ): StateDeleteResponseModel {
        TODO()
    }
}