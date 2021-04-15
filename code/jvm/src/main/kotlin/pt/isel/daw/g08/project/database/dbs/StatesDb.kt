package pt.isel.daw.g08.project.database.dbs

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.StateDao

private const val GET_ALL_STATES_QUERY = "SELECT sid, name, is_start, project_id, project_name, author_id, author_name FROM V_STATE"

private const val GET_STATES_FROM_PROJECT_QUERY = "$GET_ALL_STATES_QUERY WHERE project_id = :pid"
private const val GET_STATES_COUNT = "SELECT COUNT(sid) as count FROM STATE WHERE project = :pid"
private const val GET_STATE_QUERY = "$GET_ALL_STATES_QUERY WHERE sid = :sid"

private const val GET_NEXT_STATES_QUERY = "$GET_ALL_STATES_QUERY WHERE sid IN (SELECT to_sid FROM STATETRANSITION WHERE from_sid = :sid)"
private const val GET_NEXT_STATES_COUNT = "SELECT COUNT(to_sid) as count FROM STATETRANSITION WHERE from_sid = :sid"

@Component
class StatesDb : DatabaseHelper() {
    fun getAllStates(page: Int, perPage: Int, projectId: Int) =
        boundedGetList(page, perPage, GET_STATES_FROM_PROJECT_QUERY, "pid", projectId, StateDao::class.java)

    fun getStatesCount(projectId: Int) = boundedGetOne("pid", projectId, GET_STATES_COUNT, Int::class.java)
    fun getState(stateId: Int) = boundedGetOne("sid", stateId, GET_STATE_QUERY, StateDao::class.java)


    fun getNextStates(page: Int, perPage: Int, stateId: Int) =
        boundedGetList(page, perPage, GET_NEXT_STATES_QUERY, "sid", stateId, StateDao::class.java)
    fun getNextStatesCount(stateId: Int) = boundedGetOne("sid", stateId, GET_NEXT_STATES_COUNT, Int::class.java)
}