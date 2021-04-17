package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.controllers.models.StateInputModel
import pt.isel.daw.g08.project.controllers.models.StateOutputModel
import pt.isel.daw.g08.project.controllers.models.StatesOutputModel
import pt.isel.daw.g08.project.database.helpers.StatesDb
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenActionField
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.checkbox
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.hidden
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.number
import pt.isel.daw.g08.project.responses.siren.SirenFieldType.text
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping("${PROJECTS_HREF}/{projectId}/states")
class StatesController(val db: StatesDb) : BaseController() {

    @GetMapping
    fun getAllStates(
        @PathVariable projectId: Int,
        @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) page: Int,
        @RequestParam(defaultValue = COUNT_DEFAULT_VALUE) count: Int
    ): ResponseEntity<Response> {
        val statesDao = db.getAllStatesFromProject(page, count, projectId)
        val collectionSize = db.getStatesCount(projectId)
        val states = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = page,
            pageSize = statesDao.size
        )

        val baseUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}"

        return createResponseEntity(
            states.toSirenObject(
                entities = statesDao.map { stateDao ->
                   StateOutputModel(
                       id = stateDao.sid,
                       name = stateDao.name,
                       isStartState = stateDao.is_start,
                       project = stateDao.project_name,
                       author = stateDao.author_name,
                   ).toSirenObject(
                       rel = listOf("item"),
                       links = listOf(
                           // self, auth,  proj,nextstates, states,
                           SirenLink(rel = listOf("self"), href = URI("${baseUri}/states/${stateDao.sid}")),
                           SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${stateDao.author_id}")),
                           SirenLink(rel = listOf("project"), href = URI(baseUri)),
                           SirenLink(rel = listOf("nextStates"), href = URI("${baseUri}/states/${stateDao.sid}/nextStates")),
                           SirenLink(rel = listOf("states"), href = URI("${baseUri}/states/")),
                       )
                   )
                },
                actions = listOf(
                    SirenAction(
                        name = "create-state",
                        title = "Create State",
                        method = HttpMethod.PUT,
                        href = URI("${baseUri}/states"),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    )
                ),
                links = createUriListForPagination("${baseUri}/states", page, states.pageSize, count, collectionSize)
            ),
            200
        )
    }

    @GetMapping("{stateId}")
    fun getState(
        @PathVariable projectId: Int,
        @PathVariable stateId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val stateDao = db.getStateById(stateId)
        val state = StateOutputModel(
            id = stateDao.sid,
            name = stateDao.name,
            isStartState = stateDao.is_start,
            project = stateDao.project_name,
            author = stateDao.author_name,
        )

        val baseUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}"
        val selfUri = URI("${baseUri}/states/${state.id}")

        return createResponseEntity(
            state.toSirenObject(
                actions = listOf(
                    SirenAction(
                        name = "edit-state",
                        title = "Edit State",
                        method = HttpMethod.PUT,
                        href = selfUri,
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = state.id),
                            SirenActionField(name = "name", type = text),
                            SirenActionField(name = "isStart", type = checkbox),
                        )
                    ),
                    SirenAction(
                        name = "delete-state",
                        title = "Delete State",
                        method = HttpMethod.DELETE,
                        href = selfUri,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = stateId),
                        )
                    ),
                ),
                links = listOf(
                    SirenLink(rel = listOf("self"), href = URI("${baseUri}/states/${stateDao.sid}")),
                    SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${stateDao.author_id}")),
                    SirenLink(rel = listOf("project"), href = URI(baseUri)),
                    SirenLink(rel = listOf("nextStates"), href = URI("${baseUri}/states/${stateDao.sid}/nextStates")),
                    SirenLink(rel = listOf("states"), href = URI("${baseUri}/states/")),
                )
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
        val statesDao = db.getNextStates(page, count, stateId)
        val collectionSize = db.getNextStatesCount(stateId)
        val states = StatesOutputModel(
            collectionSize = collectionSize,
            pageIndex = page,
            pageSize = statesDao.size
        )

        val baseUri = "${env.getBaseUrl()}/${PROJECTS_HREF}/${projectId}"

        return createResponseEntity(
            states.toSirenObject(
                entities = statesDao.map { stateDao ->
                    StateOutputModel(
                        id = stateDao.sid,
                        name = stateDao.name,
                        isStartState = stateDao.is_start,
                        project = stateDao.project_name,
                        author = stateDao.author_name,
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = URI("${baseUri}/states/${stateDao.sid}")),
                            SirenLink(rel = listOf("author"), href = URI("${env.getBaseUrl()}/${USERS_HREF}/${stateDao.author_id}")),
                            SirenLink(rel = listOf("project"), href = URI(baseUri)),
                            SirenLink(rel = listOf("nextStates"), href = URI("${baseUri}/states/${stateDao.sid}/nextStates")),
                            SirenLink(rel = listOf("states"), href = URI("${baseUri}/states/")),
                        ),
                        actions = listOf(
                            SirenAction(
                                name = "delete-next-state",
                                title = "Delete Next State",
                                method = HttpMethod.DELETE,
                                href = URI("${baseUri}/states/${stateId}/nextStates/${stateDao.sid}"),
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
                        href = URI("${baseUri}/states/${stateId}/nextStates"),
                        type = INPUT_CONTENT_TYPE,
                        fields = listOf(
                            SirenActionField(name = "projectId", type = hidden, value = projectId),
                            SirenActionField(name = "stateId", type = hidden, value = stateId),
                            SirenActionField(name = "nextStateId", type = number),
                        )
                    )
                ),
                links = createUriListForPagination("${baseUri}/states", page, states.pageSize, count, collectionSize)
            ),
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