package pt.isel.daw.g08.project.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.isel.daw.g08.project.controllers.models.UserOutputModel
import pt.isel.daw.g08.project.controllers.models.UsersOutputModel
import pt.isel.daw.g08.project.database.dbs.UsersDb
import pt.isel.daw.g08.project.responses.Response

@RestController
@RequestMapping(USERS_HREF)
class UsersController(val db: UsersDb) : BaseController() {

    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<Response> {
        val users = db.getAllUsers(page, count)
        val collectionSize = db.getUsersCount()

        return createResponseEntity(
            UsersOutputModel(
                selfUrl = "${env.getBaseUrl()}/${USERS_HREF}?page=${page}&count=${count}",
                prevUrl = "${env.getBaseUrl()}/${USERS_HREF}?page=${page - 1}&count=${count}",
                nextUrl = "${env.getBaseUrl()}/${USERS_HREF}?page=${page + 1}&count=${count}",
                templateUrl = "${env.getBaseUrl()}/${USERS_HREF}{?pageIndex,pageSize}",
                collectionSize = collectionSize,
                pageIndex = page,
                pageSize = users.size,
                users = users.map {
                    UserOutputModel(
                        id = it.uid,
                        name = it.username,
                        selfUrl = "${env.getBaseUrl()}/${USERS_HREF}/${it.uid}",
                        usersUrl = "${env.getBaseUrl()}/${USERS_HREF}",
                        isCollectionItem = true
                    )
                }),
            200
        )
    }

    @GetMapping("{userId}")
    fun getUser(
        @PathVariable userId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val user = db.getUser(userId)

        return createResponseEntity(
            UserOutputModel(
                id = user.uid,
                name = user.username,
                selfUrl = "${env.getBaseUrl()}/${USERS_HREF}/${user.uid}",
                usersUrl = "${env.getBaseUrl()}/${USERS_HREF}",
            ),
            200
        )
    }
}