package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.State

private const val GET_STATES_BASE = "SELECT sid, number, name, is_start, project_id, project_name, author_id, author_name FROM V_STATE"

private const val GET_STATES_FROM_PROJECT_QUERY = "$GET_STATES_BASE WHERE project_id = :pid ORDER BY number"
private const val GET_STATES_COUNT = "SELECT COUNT(sid) as count FROM STATE WHERE project = :pid"
private const val GET_STATE_QUERY = "$GET_STATES_BASE WHERE project_id = :projectId AND number = :stateNumber"

private const val GET_NEXT_STATES_QUERY =
    "$GET_STATES_BASE WHERE sid IN (SELECT to_sid FROM STATETRANSITION WHERE from_sid IN " +
            "(SELECT sid FROM STATE WHERE project = :projectId AND number = :stateNumber)) ORDER BY V_STATE.number"

private const val GET_NEXT_STATES_COUNT =
    "SELECT COUNT(to_sid) as count FROM STATETRANSITION WHERE from_sid IN (SELECT sid FROM STATE WHERE project = :projectId AND number = :stateNumber)"

@Component
class StatesDb(val jdbi: Jdbi) {
    fun getAllStatesFromProject(page: Int, perPage: Int, projectId: Int) =
        jdbi.getList(GET_STATES_FROM_PROJECT_QUERY, State::class.java, page, perPage, mapOf("pid" to projectId))

    fun getStatesCount(projectId: Int) = jdbi.getOne(GET_STATES_COUNT, Int::class.java, mapOf("pid" to projectId))

    fun getStateByNumber(projectId: Int, stateNumber: Int) =
        jdbi.getOne(GET_STATE_QUERY, State::class.java,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )

    fun getNextStates(page: Int, perPage: Int, projectId: Int, stateNumber: Int) =
        jdbi.getList(GET_NEXT_STATES_QUERY, State::class.java, page, perPage,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )
    fun getNextStatesCount(projectId: Int, stateNumber: Int) = jdbi.getOne(GET_NEXT_STATES_COUNT, Int::class.java,
        mapOf(
            "projectId" to projectId,
            "stateNumber" to stateNumber
        )
    )
}