package pt.isel.daw.g08.project.dao

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.Timestamp

class IssueDao(
    val iid: Int,
    val state: String,
    val project: String,
    val name: String,
    val description: String,
    val createDate: Timestamp,
    val closeDate: Timestamp?,
    val labels: List<String>,
) {
    companion object {
        fun getRowMapper(rs: ResultSet, ctx: StatementContext): IssueDao {
            TODO()
        }
    }
}
