package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.StateDao

private const val GET_STATES_BASE = "SELECT sid, name, is_start, project_id, project_name, author_id, author_name FROM V_STATE"

private const val GET_STATES_FROM_PROJECT_QUERY = "$GET_STATES_BASE WHERE project_id = :pid ORDER BY sid"
private const val GET_STATES_COUNT = "SELECT COUNT(sid) as count FROM STATE WHERE project = :pid"
private const val GET_STATE_QUERY = "$GET_STATES_BASE WHERE sid = :sid"

private const val GET_NEXT_STATES_QUERY = "$GET_STATES_BASE WHERE sid IN (SELECT to_sid FROM STATETRANSITION WHERE from_sid = :sid) ORDER BY sid"
private const val GET_NEXT_STATES_COUNT = "SELECT COUNT(to_sid) as count FROM STATETRANSITION WHERE from_sid = :sid"

@Component
class StatesDb : DatabaseHelper() {
    fun getAllStatesFromProject(page: Int, perPage: Int, projectId: Int) =
        getList(GET_STATES_FROM_PROJECT_QUERY, StateDao::class.java, page, perPage, Pair("pid", projectId))

    fun getStatesCount(projectId: Int) = getOne(GET_STATES_COUNT, Int::class.java, Pair("pid", projectId))
    fun getStateById(stateId: Int) = getOne(GET_STATE_QUERY, StateDao::class.java, Pair("sid", stateId))


    fun getNextStates(page: Int, perPage: Int, stateId: Int) =
        getList(GET_NEXT_STATES_QUERY, StateDao::class.java, page, perPage, Pair("sid", stateId))
    fun getNextStatesCount(stateId: Int) = getOne(GET_NEXT_STATES_COUNT, Int::class.java, Pair("sid", stateId))
}