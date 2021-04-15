package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.*
import pt.isel.daw.g08.project.database.dbs.StatesDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/states")
class StatesController(val db: StatesDb) : BaseController() {

    @GetMapping
    fun getAllStates(
        @PathVariable projectId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<Response> {
        val states = db.getAllStates(page, count, projectId)
        val collectionSize = db.getStatesCount(projectId)

        return createResponseEntity(
            StatesOutputModel(
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states{?pageIndex,pageSize}",
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = states.size,
                states = states.map {
                    StateOutputModel(
                        id = it.sid,
                        name = it.name,
                        isStartState = it.is_start,
                        projectName = it.project_name,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.sid}",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                        nextStatesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.sid}/nextStates",
                        statesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @GetMapping("{stateId}")
    fun getState(
        @PathVariable projectId: Int,
        @PathVariable stateId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val state = db.getState(stateId)

        return createResponseEntity(
            StateOutputModel(
                id = state.sid,
                name = state.name,
                isStartState = state.is_start,
                projectName = state.project_name,
                authorName = state.author_name,
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${state.sid}",
                authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${state.author_id}",
                projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                nextStatesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${state.sid}/nextStates",
                statesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states",
            ),
            200
        )
    }

    @GetMapping("{stateId}/nextStates")
    fun getNextStates(
        @PathVariable projectId: Int,
        @PathVariable stateId: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<Response> {
        val states = db.getNextStates(page, count, stateId)
        val collectionSize = db.getNextStatesCount(stateId)

        return createResponseEntity(
            StatesOutputModel(
                selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states{?pageIndex,pageSize}",
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = states.size,
                states = states.map {
                    StateOutputModel(
                        id = it.sid,
                        name = it.name,
                        isStartState = it.is_start,
                        projectName = it.project_name,
                        authorName = it.author_name,
                        selfUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.sid}",
                        authorUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.author_id}",
                        projectUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}",
                        nextStatesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states/${it.sid}/nextStates",
                        statesUrl = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}/states",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @PutMapping
    fun createState(
        @PathVariable projectName: String,
        @RequestBody input: StateInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }

    @PutMapping("{stateName}")
    fun editState(
        @PathVariable projectName: String,
        @PathVariable stateName: String,
        @RequestBody input: StateInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }

    @DeleteMapping("{stateName}")
    fun deleteState(
        @PathVariable projectName: String,
        @PathVariable stateName: String,
    ): ResponseEntity<Any> {
        TODO()
    }
}