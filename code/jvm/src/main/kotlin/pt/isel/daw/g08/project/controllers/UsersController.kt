package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes.USERS_HREF
import pt.isel.daw.g08.project.Routes.USER_BY_ID_HREF
import pt.isel.daw.g08.project.Routes.USER_HREF
import pt.isel.daw.g08.project.Routes.createSirenLinkListForPagination
import pt.isel.daw.g08.project.Routes.getUserByIdUri
import pt.isel.daw.g08.project.controllers.models.UserOutputModel
import pt.isel.daw.g08.project.controllers.models.UsersOutputModel
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.UsersDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.pipeline.interceptors.RequiresAuth
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenLink
import pt.isel.daw.g08.project.responses.toResponseEntity
import java.net.URI

@RestController
class UsersController(val db: UsersDb) {

    @RequiresAuth
    @GetMapping(USERS_HREF)
    fun getAllUsers(
        pagination: Pagination
    ): ResponseEntity<Response> {
        val users = db.getAllUsers(pagination.page, pagination.limit)
        val collectionSize = db.getUsersCount()
        val usersModel = UsersOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = users.size
        )

        return usersModel.toSirenObject(
            entities = users.map {
                UserOutputModel(
                    id = it.uid,
                    name = it.username
                ).toSirenObject(
                    rel = listOf("item"),
                    links = listOf(
                        SirenLink(rel = listOf("self"), href = getUserByIdUri(it.uid)),
                        SirenLink(rel = listOf("users"), href = URI(USERS_HREF))
                    ),
                )
            },
            links = createSirenLinkListForPagination(
                URI(USERS_HREF),
                pagination.page,
                pagination.limit,
                collectionSize
            )
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(USER_BY_ID_HREF)
    fun getUser(
        @PathVariable userId: Int,
    ): ResponseEntity<Response> {
        val user = db.getUserById(userId)
        val userModel = UserOutputModel(
            id = user.uid,
            name = user.username
        )

        return userModel.toSirenObject(
            links = listOf(
                SirenLink(rel = listOf("self"), href = getUserByIdUri(user.uid)),
                SirenLink(rel = listOf("users"), href = URI(USERS_HREF))
            ),
        ).toResponseEntity(HttpStatus.OK)
    }

    @RequiresAuth
    @GetMapping(USER_HREF)
    fun getAuthenticatedUser(
        user: User,
    ): ResponseEntity<Response> {
        val userModel = UserOutputModel(
            id = user.uid,
            name = user.username,
        )

        return userModel.toSirenObject(
            links = listOf(
                SirenLink(rel = listOf("self"), href = getUserByIdUri(user.uid)),
                SirenLink(rel = listOf("users"), href = URI(USERS_HREF))
            ),
        ).toResponseEntity(HttpStatus.OK)
    }
}