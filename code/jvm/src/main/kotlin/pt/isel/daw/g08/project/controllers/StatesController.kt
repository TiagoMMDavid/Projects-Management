package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes
import pt.isel.daw.g08.project.Routes.INPUT_CONTENT_TYPE
import pt.isel.daw.g08.project.Routes.NEXT_STATES_HREF
import pt.isel.daw.g08.project.Routes.NEXT_STATE_BY_NUMBER_HREF
import pt.isel.daw.g08.project.Routes.NEXT_STATE_PARAM
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
import pt.isel.daw.g08.project.controllers.models.NextStateInputModel
import pt.isel.daw.g08.project.controllers.models.StateInputModel
import pt.isel.daw.g08.project.controllers.models.StateOutputModel
import pt.isel.daw.g08.project.controllers.models.StatesOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.StatesDb
import pt.isel.daw.g08.project.exceptions.InvalidInputException
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.checkbox
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
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

        val statesUri = getStatesUri(projectId)

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
                           SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, it.number)),
                           SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id)),
                           SirenLink(rel = listOf("project"), href = getProjectByIdUri(it.project_id)),
                           SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, it.number)),
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
                    pagination.limit,
                    collectionSize
                ) + listOf(SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId)))
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
                        href = getStateByNumberUri(projectId, stateNumber),
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
                        href = getStateByNumberUri(projectId, stateNumber),
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateNumber", type = hidden, value = stateNumber),
                        )
                    ),
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, stateNumber)),
                    SirenLink(rel = listOf("author"), href = getUserByIdUri(state.author_id)),
                    SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId)),
                    SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, stateNumber)),
                    SirenLink(rel = listOf("states"), href = getStatesUri(projectId)),
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

        val nextStatesUri = getNextStatesUri(projectId, stateNumber)

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
                            SirenLink(rel = listOf("self"), href = getStateByNumberUri(projectId, it.number)),
                            SirenLink(rel = listOf("author"), href = getUserByIdUri(it.author_id)),
                            SirenLink(rel = listOf("project"), href = getProjectByIdUri(it.project_id)),
                            SirenLink(rel = listOf("nextStates"), href = getNextStatesUri(projectId, it.number)),
                            SirenLink(rel = listOf("states"), href = getStatesUri(projectId)),
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
                                    SirenActionField(name = "nextStateNumber", type = hidden, value = it.sid),
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
                            SirenActionField(name = "state", type = text),
                        )
                    )
                ),
                links = Routes.createSirenLinkListForPagination(
                    nextStatesUri,
                    pagination.page,
                    pagination.limit,
                    collectionSize
                ) + listOf(
                    SirenLink(rel = listOf("project"), href = getProjectByIdUri(projectId)),
                    SirenLink(rel = listOf("state"), href = getStateByNumberUri(projectId, stateNumber))
                )
            ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @PostMapping(STATES_HREF)
    fun createState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        user: User,
        @RequestBody input: StateInputModel,
    ): ResponseEntity<Any> {
        if (input.name == null) throw InvalidInputException("Missing name")
        if (input.isStart == null) throw InvalidInputException("Missing isStart")

        val state = db.createState(projectId, user.uid, input.name, input.isStart)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", Routes.getStateByNumberUri(projectId, state.number).toString())
            .body(null)
    }

    @RequiresAuth
    @PutMapping(STATE_BY_NUMBER_HREF)
    fun editState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
        @RequestBody input: StateInputModel,
    ): ResponseEntity<Any> {
        if (input.name == null && input.isStart == null) throw InvalidInputException("Missing new name or new isStart")

        db.editState(projectId, stateNumber, input.name, input.isStart)
        return ResponseEntity
            .status(HttpStatus.OK)
            .header("Location", getStateByNumberUri(projectId, stateNumber).toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(STATE_BY_NUMBER_HREF)
    fun deleteState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
    ): ResponseEntity<Any> {
        db.deleteState(projectId, stateNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }

    @RequiresAuth
    @PutMapping(NEXT_STATES_HREF)
    fun addNextState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
        @RequestBody input: NextStateInputModel
    ): ResponseEntity<Any> {
        if (input.state == null) throw InvalidInputException("Missing state")
        db.addNextState(projectId, stateNumber, input.state)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .header("Location", getNextStatesUri(projectId, stateNumber).toString())
            .body(null)
    }

    @RequiresAuth
    @DeleteMapping(NEXT_STATE_BY_NUMBER_HREF)
    fun deleteNextState(
        @PathVariable(name = PROJECT_PARAM) projectId: Int,
        @PathVariable(name = STATE_PARAM) stateNumber: Int,
        @PathVariable(name = NEXT_STATE_PARAM) nextStateNumber: Int,
    ): ResponseEntity<Any> {
        db.deleteNextState(projectId, stateNumber, nextStateNumber)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(null)
    }
}