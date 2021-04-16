package pt.isel.daw.g08.project.database.helpers

import org.springframework.stereotype.Component
import pt.isel.daw.g08.project.database.dao.UserDao

private const val GET_ALL_USERS_QUERY = "SELECT uid, username, pass FROM USERS"
private const val GET_USERS_COUNT = "SELECT COUNT(uid) as count FROM USERS"
private const val GET_USER_QUERY = "$GET_ALL_USERS_QUERY WHERE uid = :uid"

@Component
class UsersDb : DatabaseHelper() {
    fun getAllUsers(page: Int, perPage: Int) = getList(page, perPage, GET_ALL_USERS_QUERY, UserDao::class.java)
    fun getUsersCount() = getOne(GET_USERS_COUNT, Int::class.java)
    fun getUserById(userId: Int) = boundedGetOne("uid", userId, GET_USER_QUERY, UserDao::class.java)
}