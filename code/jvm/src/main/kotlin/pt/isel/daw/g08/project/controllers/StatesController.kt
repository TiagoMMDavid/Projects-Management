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
import pt.isel.daw.g08.project.Routes
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.NEXT_STATES_HREF
import pt.isel.daw.g08.project.Routes.STATES_HREF
import pt.isel.daw.g08.project.Routes.STATE_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.getNextStateByIdUri
import pt.isel.daw.g08.project.Routes.getNextStatesUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getStateByIdUri
import pt.isel.daw.g08.project.Routes.getStatesUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.controllers.models.StateInputModel
import pt.isel.daw.g08.project.controllers.models.StateOutputModel
import pt.isel.daw.g08.project.controllers.models.StatesOutputModel
import pt.isel.daw.g08.project.database.helpers.StatesDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.checkbox
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.number
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity
import java.net.URI

@RestController
class StatesController(val db: StatesDb) {

    @GetMapping(STATES_HREF)
    fun getAllStates(
        @PathVariable projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val states = db.getAllStatesFromProject(pagination.page, pagination.limit, projectId)

        val collectionSize = db.getStatesCount(projectId)
        val statesModel = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = states.size
        )

        return statesModel.toSirenObject(
                entities = states.map { state ->
                   StateOutputModel(
                       id = state.sid,
                       name = state.name,
                       isStartState = state.is_start,
                       project = state.project_name,
                       author = state.author_name,
                   ).toSirenObject(
                       rel = listOf("item"),
                       links = listOf(
                           SirenLink(rel = listOf("self"), href = getStateByIdUri(projectId, state.sid).includeHost()),
                           SirenLink(rel = listOf("author"), href = getUserByIdUri(state.author_id).includeHost()),
                           SirenLink(rel = listOf("project"), href = getProjectByIdUri(state.project_id).includeHost()),
                           SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, state.sid).includeHost()),
                           SirenLink(rel = listOf("states"), href = getStatesUri(projectId).includeHost()),
                       )
                   )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-state",
                        title = "Create State",
                        method = HttpMethod.PUT,
                        href = getStatesUri(projectId),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    )
                ),
                links = Routes.createSirenLinkListForPagination(
                    URI(STATES_HREF).includeHost(),
                    pagination.page,
                    statesModel.pageSize,
                    pagination.limit,
                    collectionSize
                )
            ).toResponseEntity(HttpStatus.OK)
    }

    @GetMapping(STATE_BY_ID_HREF)
    fun getState(
        @PathVariable projectId: Int,
        @PathVariable stateId: Int,
    ): ResponseEntity<Response> {
        val state = db.getStateById(stateId)
        val stateModel = StateOutputModel(
            id = state.sid,
            name = state.name,
            isStartState = state.is_start,
            project = state.project_name,
            author = state.author_name,
        )

        return stateModel.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-state",
                        title = "Edit State",
                        method = HttpMethod.PUT,
                        href = getStateByIdUri(projectId, stateId).includeHost(),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = stateModel.id),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    ),
                    SirenAction(
                        name = "delete-state",
                        title = "Delete State",
                        method = HttpMethod.DELETE,
                        href = getStateByIdUri(projectId, stateId).includeHost(),
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = stateId),
                        )
                    ),
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = getStateByIdUri(projectId, stateId).includeHost()),
                    SirenLink(rel = listOf("author"), href = getUserByIdUri(state.author_id).includeHost()),
                    SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                    SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, stateId).includeHost()),
                    SirenLink(rel = listOf("states"), href = getStatesUri(projectId).includeHost()),
                )
            ).toResponseEntity(HttpStatus.OK)
    }

    @GetMapping(NEXT_STATES_HREF)
    fun getNextStates(
        @PathVariable projectId: Int,
        @PathVariable stateId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val states = db.getNextStates(pagination.page, pagination.limit, stateId)
        val collectionSize = db.getNextStatesCount(stateId)
        val statesModel = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = states.size
        )

        return statesModel.toSirenObject(
                entities = states.map { state ->
                    StateOutputModel(
                        id = state.sid,
                        name = state.name,
                        isStartState = state.is_start,
                        project = state.project_name,
                        author = state.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = getStateByIdUri(projectId, state.sid).includeHost()),
                            SirenLink(rel = listOf("author"), href = getUserByIdUri(state.author_id).includeHost()),
                            SirenLink(rel = listOf("project"), href = getProjectByIdUri(state.project_id).includeHost()),
                            SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, state.sid).includeHost()),
                            SirenLink(rel = listOf("states"), href = getStatesUri(projectId).includeHost()),
                        ),
                        actions = listOf(
                            SirenAction(
                                name = "delete-next-state",
                                title = "Delete Next State",
                                method = HttpMethod.DELETE,
                                href = getNextStateByIdUri(projectId, stateId, state.sid),
                                type = INPUT_CONTENT_TYPE,
                                fields = listOf(
                                    SirenActionField(name = "projectId", type = hidden, value = projectId),
                                    SirenActionField(name = "stateId", type = hidden, value = stateId),
                                )
                            )
                        )
                    )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-next-state",
                        title = "Create Next State",
                        method = HttpMethod.PUT,
                        href = getNextStatesUri(projectId, stateId),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = stateId),
                            SirenActionField(name = "nextStateId", type = number),
                        )
                    )
                ),
                links = Routes.createSirenLinkListForPagination(
                    URI(NEXT_STATES_HREF).includeHost(),
                    pagination.page,
                    statesModel.pageSize,
                    pagination.limit,
                    collectionSize
                )
            ).toResponseEntity(HttpStatus.OK)
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