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
import pt.isel.daw.g08.project.Routes.PROJECT_PARAM
import pt.isel.daw.g08.project.Routes.STATES_HREF
import pt.isel.daw.g08.project.Routes.STATE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.STATE_PARAM
import pt.isel.daw.g08.project.Routes.getNextStateByNumberUri
import pt.isel.daw.g08.project.Routes.getNextStatesUri
import pt.isel.daw.g08.project.Routes.getProjectByIdUri
import pt.isel.daw.g08.project.Routes.getStateByNumberUri
import pt.isel.daw.g08.project.Routes.getStatesUri
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.Routes.includeHost
import pt.isel.daw.g08.project.controllers.models.StateInputModel
import pt.isel.daw.g08.project.controllers.models.StateOutputModel
import pt.isel.daw.g08.project.controllers.models.StatesOutputModel
import pt.isel.daw.g08.project.database.helpers.StatesDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.checkbox
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.number
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity

@RestController
class StatesController(val db: StatesDb) {

    @RequiresAuth
    @GetMapping(STATES_HREF)
    fun getAllStates(
        @PathVariable(PROJECT_PARAM) projectId: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val states = db.getAllStatesFromProject(pagination.page, pagination.limit, projectId)

        val collectionSize = db.getStatesCount(projectId)
        val statesModel = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = states.size
        )

        val statesUri = getStatesUri(projectId).includeHost()

        return statesModel.toSirenObject(
                entities = states.map {
                   StateOutputModel(
                       id = it.sid,
                       number = it.number,
                       name = it.name,
                       isStartState = it.is_start,
                       project = it.project_name,
                       author = it.author_name,
                   ).toSirenObject(
                       rel = listOf("item"),
                       links = listOf(
                           SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, it.number).includeHost()),
                           SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id).includeHost()),
                           SirenLink(rel = listOf("project"), href = getProjectByIdUri(it.project_id).includeHost()),
                           SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, it.number).includeHost()),
                           SirenLink(rel = listOf("states"), href = statesUri),
                       )
                   )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-state",
                        title = "Create State",
                        method = HttpMethod.PUT,
                        href = statesUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    )
                ),
                links = Routes.createSirenLinkListForPagination(
                    statesUri,
                    pagination.page,
                    statesModel.pageSize,
                    pagination.limit,
                    collectionSize
                ) + listOf(SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()))
            ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(STATE_BY_NUMBER_HREF)
    fun getState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
    ): ResponseEntity<Response> {
        val state = db.getStateByNumber(projectId, stateNumber)
        val stateModel = StateOutputModel(
            id = state.sid,
            number = state.number,
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
                        href = getStateByNumberUri(projectId, stateNumber).includeHost(),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateNumber", type = hidden, value = stateModel.number),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    ),
                    SirenAction(
                        name = "delete-state",
                        title = "Delete State",
                        method = HttpMethod.DELETE,
                        href = getStateByNumberUri(projectId, stateNumber).includeHost(),
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateNumber", type = hidden, value = stateNumber),
                        )
                    ),
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, stateNumber).includeHost()),
                    SirenLink(rel = listOf("author"), href = getUserByIdUri(state.author_id).includeHost()),
                    SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                    SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, stateNumber).includeHost()),
                    SirenLink(rel = listOf("states"), href = getStatesUri(projectId).includeHost()),
                )
            ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(NEXT_STATES_HREF)
    fun getNextStates(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
        pagination: Pagination
    ): ResponseEntity<Response> {
        val states = db.getNextStates(pagination.page, pagination.limit, projectId, stateNumber)
        val collectionSize = db.getNextStatesCount(projectId, stateNumber)
        val statesModel = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = states.size
        )

        val nextStatesUri = getNextStatesUri(projectId, stateNumber).includeHost()

        return statesModel.toSirenObject(
                entities = states.map {
                    StateOutputModel(
                        id = it.sid,
                        number = it.number,
                        name = it.name,
                        isStartState = it.is_start,
                        project = it.project_name,
                        author = it.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, it.number).includeHost()),
                            SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id).includeHost()),
                            SirenLink(rel = listOf("project"), href = getProjectByIdUri(it.project_id).includeHost()),
                            SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, it.number).includeHost()),
                            SirenLink(rel = listOf("states"), href = getStatesUri(projectId).includeHost()),
                        ),
                        actions = listOf(
                            SirenAction(
                                name = "delete-next-state",
                                title = "Delete Next State",
                                method = HttpMethod.DELETE,
                                href = getNextStateByNumberUri(projectId, stateNumber, it.sid),
                                type = INPUT_CONTENT_TYPE,
                                fields = listOf(
                                    SirenActionField(name = "projectId", type = hidden, value = projectId),
                                    SirenActionField(name = "stateNumber", type = hidden, value = stateNumber),
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
                        href = nextStatesUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateNumber", type = hidden, value = stateNumber),
                            SirenActionField(name = "nextStateId", type = number),
                        )
                    )
                ),
                links = Routes.createSirenLinkListForPagination(
                    nextStatesUri,
                    pagination.page,
                    statesModel.pageSize,
                    pagination.limit,
                    collectionSize
                ) + listOf(
                    SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId).includeHost()),
                    SirenLink(rel = listOf("state"), href = getStateByNumberUri(projectId, stateNumber).includeHost())
                )
            ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PutMapping(STATES_HREF)
    fun createState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        input: StateInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }

    @RequiresAuth
    @PutMapping(STATE_BY_NUMBER_HREF)
    fun editState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
        input: StateInputModel,
    ): ResponseEntity<Any> {
        TODO()
    }

    @RequiresAuth
    @DeleteMapping(STATE_BY_NUMBER_HREF)
    fun deleteState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable stateId: Int,
    ): ResponseEntity<Any> {
        TODO()
    }
}