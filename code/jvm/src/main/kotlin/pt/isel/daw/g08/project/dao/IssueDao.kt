package pt.isel.daw.g08.project.dao

import org.jdbi.v3.core.Jdbi
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
        fun mapRow(rs: ResultSet, ctx: StatementContext, jdbi: Jdbi): IssueDao {
            val iid = rs.getInt("iid")

            // Get state name from id
            val stateName = jdbi.withHandle<String, Exception> {
                it.createQuery("SELECT name FROM STATE WHERE sid=:sid")
                    .bind("sid", rs.getInt("state"))
                    .mapTo(String::class.java)
                    .first()
            }

            // Get labels of issue
            val labels = jdbi.withHandle<List<String>, Exception> {
                it.createQuery("SELECT label_name FROM ISSUE_LABEL WHERE iid=:iid")
                    .bind("iid", iid)
                    .mapTo(String::class.java)
                    .list()
            }

            // Create Issue instance
            return IssueDao(
                iid = iid,
                state = stateName,
                project = rs.getString("project"),
                name = rs.getString("name"),
                description = rs.getString("description"),
                createDate = rs.getTimestamp("create_date"),
                closeDate = rs.getTimestamp("close_date"),
                labels = labels
            )
        }
    }
}