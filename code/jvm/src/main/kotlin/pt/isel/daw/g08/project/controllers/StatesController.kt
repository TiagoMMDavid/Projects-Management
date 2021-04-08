package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import pt.isel.daw.g08.project.controllers.models.*

@RestController
@RequestMapping("/api/projects/{projectName}/states")
class StatesController(val jdbi: Jdbi) {

    @GetMapping
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

    @PutMapping
    fun createState(
        @PathVariable projectName: String,
        @RequestBody input: StateInputModel,
    ): StateCreateOutputModel {
        TODO()
    }

    @PutMapping("{stateName}")
    fun editState(
        @PathVariable projectName: String,
        @PathVariable stateName: String,
        @RequestBody input: StateInputModel,
    ): StateCreateOutputModel {
        TODO()
    }

    @DeleteMapping("{stateName}")
    fun deleteState(
        @PathVariable projectName: String,
        @PathVariable stateName: String,
    ): StateDeleteOutputModel {
        TODO()
    }
}