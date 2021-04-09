package pt.isel.daw.g08.project.dao

import org.jdbi.v3.core.Jdbi
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
        fun mapRow(rs: ResultSet, ctx: StatementContext, jdbi: Jdbi): StateDao {
            val sid = rs.getInt("sid")

            // Get next states
            val nextStates = jdbi.withHandle<List<String>, Exception> {
                it.createQuery("SELECT name FROM STATE JOIN STATETRANSITION ON sid=to_sid WHERE from_sid=:sid")
                    .bind("sid", sid)
                    .mapTo(String::class.java)
                    .list()
            }

            // Create State instance
            return StateDao(
                sid = sid,
                project = rs.getString("project"),
                name = rs.getString("name"),
                isStart = rs.getBoolean("is_start"),
                nextStates = nextStates
            )
        }
    }
}
