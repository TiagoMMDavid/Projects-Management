package pt.isel.daw.g08.project.dao

import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class StateDao(
    val sid: Int,
    val project: String,
    val name: String,
    val isStart: Boolean,
    val nextStates: List<String>, // Will be the result of a join operation
) {
    companion object {
        fun getRowMapper(rs: ResultSet, ctx: StatementContext): StateDao {
            TODO()
        }
    }
}
