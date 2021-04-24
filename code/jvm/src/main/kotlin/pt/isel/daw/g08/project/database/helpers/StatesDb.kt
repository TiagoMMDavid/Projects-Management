package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.State

private const val GET_STATES_BASE = "SELECT sid, number, name, is_start, project_id, project_name, author_id, author_name FROM V_STATE"

private const val GET_STATES_FROM_PROJECT_QUERY = "$GET_STATES_BASE WHERE project_id = :pid ORDER BY number"
private const val GET_STATES_COUNT = "SELECT COUNT(sid) as count FROM STATE WHERE project = :pid"
private const val GET_STATE_QUERY = "$GET_STATES_BASE WHERE project_id = :projectId AND number = :stateNumber"
private const val GET_STATE_BY_NAME_QUERY = "$GET_STATES_BASE WHERE project_id = :projectId AND name = :stateName"
private const val GET_STATE_BY_ID_QUERY = "$GET_STATES_BASE WHERE sid = :sid"

private const val GET_NEXT_STATES_QUERY =
    "$GET_STATES_BASE WHERE sid IN (SELECT to_sid FROM STATETRANSITION WHERE from_sid IN " +
            "(SELECT sid FROM STATE WHERE project = :projectId AND number = :stateNumber)) ORDER BY V_STATE.number"

private const val GET_NEXT_STATES_COUNT =
    "SELECT COUNT(to_sid) as count FROM STATETRANSITION WHERE from_sid IN (SELECT sid FROM STATE WHERE project = :projectId AND number = :stateNumber)"

private const val CREATE_STATE_QUERY = "INSERT INTO STATE(project, name, is_start, author) VALUES(:project, :name, :isStart, :author)"
private const val ADD_NEXT_STATE_QUERY = "INSERT INTO STATETRANSITION(from_sid, to_sid) VALUES(:fromSid, :toSid)"

private const val EDIT_STATE_START = "UPDATE STATE SET"
private const val EDIT_STATE_END = "WHERE project = :projectId and number = :stateNumber"

private const val DELETE_STATE_QUERY = "DELETE FROM STATE WHERE project = :projectId AND number = :stateNumber"
private const val DELETE_NEXT_STATE_QUERY = "DELETE FROM STATETRANSITION WHERE from_sid = :fromSid AND to_sid = :toSid"

@Component
class StatesDb(
    val projectsDb: ProjectsDb,
    val jdbi: Jdbi
) {

    fun getAllStatesFromProject(page: Int, perPage: Int, projectId: Int): List<State> {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.getList(GET_STATES_FROM_PROJECT_QUERY, State::class.java, page, perPage, mapOf("pid" to projectId))
    }

    fun getStatesCount(projectId: Int) = jdbi.getOne(GET_STATES_COUNT, Int::class.java, mapOf("pid" to projectId))

    fun getStateByNumber(projectId: Int, stateNumber: Int) =
        jdbi.getOne(GET_STATE_QUERY, State::class.java,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )

    fun getStateByName(projectId: Int, stateName: String) =
        jdbi.getOne(
            GET_STATE_BY_NAME_QUERY, State::class.java,
            mapOf(
                "projectId" to projectId,
                "stateName" to stateName
            )
        )

    fun getNextStates(page: Int, perPage: Int, projectId: Int, stateNumber: Int): List<State> {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.getList(GET_NEXT_STATES_QUERY, State::class.java, page, perPage,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )
    }

    fun getNextStatesCount(projectId: Int, stateNumber: Int) =
        jdbi.getOne(GET_NEXT_STATES_COUNT, Int::class.java,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )

    fun createState(projectId: Int, userId: Int, name: String, isStart: Boolean): State {
        projectsDb.getProjectById(projectId) // Check if project exists (will throw exception if not found)
        return jdbi.insertAndGet(
            CREATE_STATE_QUERY, Int::class.java,
            GET_STATE_BY_ID_QUERY, State::class.java,
            mapOf(
                "name" to name,
                "project" to projectId,
                "author" to userId,
                "isStart" to isStart
            ),
            "sid"
        )
    }

    fun addNextState(projectId: Int, stateNumber: Int, toStateName: String) {
        val fromSid = getStateByNumber(projectId, stateNumber).sid
        val toSid = getStateByName(projectId, toStateName).sid

        jdbi.insert(
            ADD_NEXT_STATE_QUERY,
            mapOf(
                "fromSid" to fromSid,
                "toSid" to toSid
            )
        )
    }

    fun editState(projectId: Int, stateNumber: Int, name: String?, isStart: Boolean?) {
        if (name == null && isStart == null) {
            return
        }

        val updateFields = mutableMapOf<String, Any>()
        if (name != null) updateFields["name"] = name
        if (isStart != null) updateFields["is_start"] = isStart

        jdbi.update(
            EDIT_STATE_START,
            updateFields,
            EDIT_STATE_END,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )
    }

    fun deleteState(projectId: Int, stateNumber: Int) {
        jdbi.delete(
            DELETE_STATE_QUERY,
            mapOf(
                "projectId" to projectId,
                "stateNumber" to stateNumber
            )
        )
    }

    fun deleteNextState(projectId: Int, stateNumber: Int, nextStateNumber: Int) {
        val fromSid = getStateByNumber(projectId, stateNumber).sid
        val toSid = getStateByNumber(projectId, nextStateNumber).sid

        jdbi.delete(
            DELETE_NEXT_STATE_QUERY,
            mapOf(
                "fromSid" to fromSid,
                "toSid" to toSid
            )
        )
    }
}