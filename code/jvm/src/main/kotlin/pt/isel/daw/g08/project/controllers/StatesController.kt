package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*
import pt.isel.daw.g08.project.dao.StateDao
import pt.isel.daw.g08.project.utils.urlDecode

private const val GET_ALL_STATES_QUERY = "SELECT sid, project, name, is_start FROM STATE WHERE project = :projectName"
private const val GET_STATE_QUERY = "SELECT sid, project, name, is_start FROM STATE WHERE project = :projectName AND name = :stateName"

@RestController
@RequestMapping("/api/projects/{projectName}/states")
class StatesController(val jdbi: Jdbi) : BaseController() {

    // URL roots
    fun getStatesRoot(projectName: String) = "${env.getBaseUrl()}/api/projects/${projectName}/states"

    @GetMapping
    fun getAllStates(
        @PathVariable projectName: String,
    ): StatesOutputModel {
        val states = jdbi.withHandle<List<StateDao>, Exception> {
            it.createQuery(GET_ALL_STATES_QUERY)
                .bind("projectName", projectName.urlDecode())
                .mapTo(StateDao::class.java)
                .list()
        }

        return StatesOutputModel(states.map {
            StateOutputModel(
                name = it.name,
                isStartState = it.isStart,
                nextStates = it.nextStates,
            )
        })
    }

    @GetMapping("{stateName}")
    fun getState(
        @PathVariable projectName: String,
        @PathVariable stateName: String,
    ): StateOutputModel {
        val state = jdbi.withHandle<StateDao, Exception> {
            it.createQuery(GET_STATE_QUERY)
                .bind("projectName", projectName.urlDecode())
                .bind("stateName", stateName.urlDecode())
                .mapTo(StateDao::class.java)
                .one()
        }

        return StateOutputModel(
            name = state.name,
            isStartState = state.isStart,
            nextStates = state.nextStates,
        )
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