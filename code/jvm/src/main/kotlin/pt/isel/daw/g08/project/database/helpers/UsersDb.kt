package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.UserDao

private const val GET_USERS_BASE = "SELECT uid, username, pass FROM USERS"
private const val GET_ALL_USERS_QUERY = "$GET_USERS_BASE ORDER BY uid"
private const val GET_USERS_COUNT = "SELECT COUNT(uid) as count FROM USERS"
private const val GET_USER_QUERY = "$GET_USERS_BASE WHERE uid = :uid"

private const val GET_USER_BY_USERNAME_QUERY = "$GET_USERS_BASE WHERE username = :username"

@Component
class UsersDb : DatabaseHelper() {
    fun getAllUsers(page: Int, perPage: Int) = getList(GET_ALL_USERS_QUERY, UserDao::class.java, page, perPage)
    fun getUsersCount() = getOne(GET_USERS_COUNT, Int::class.java)
    fun getUserById(userId: Int) = getOne(GET_USER_QUERY, UserDao::class.java, Pair("uid", userId))
    fun getUserByUsername(username: String) = getOne(GET_USER_BY_USERNAME_QUERY, UserDao::class.java, Pair("username", username))
}