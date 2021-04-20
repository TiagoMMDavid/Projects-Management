package pt.isel.daw.g08.project.database.helpers

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dto.User

private const val GET_USERS_BASE = "SELECT uid, username, pass FROM USERS"
private const val GET_ALL_USERS_QUERY = "$GET_USERS_BASE ORDER BY uid"
private const val GET_USERS_COUNT = "SELECT COUNT(uid) as count FROM USERS"
private const val GET_USER_QUERY = "$GET_USERS_BASE WHERE uid = :uid"

private const val GET_USER_BY_USERNAME_QUERY = "$GET_USERS_BASE WHERE username = :username"

@Component
class UsersDb(val jdbi: Jdbi) {
    fun getAllUsers(page: Int, perPage: Int) = jdbi.getList(GET_ALL_USERS_QUERY, User::class.java, page, perPage)
    fun getUsersCount() = jdbi.getOne(GET_USERS_COUNT, Int::class.java)
    fun getUserById(userId: Int) = jdbi.getOne(GET_USER_QUERY, User::class.java, mapOf("uid" to userId))
    fun getUserByUsername(username: String) = jdbi.getOne(GET_USER_BY_USERNAME_QUERY, User::class.java, mapOf("username" to username))
}