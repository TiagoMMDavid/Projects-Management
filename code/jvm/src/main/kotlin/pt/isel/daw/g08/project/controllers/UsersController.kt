package pt.isel.daw.g08.project.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.g08.project.Routes
import pt.isel.daw.g08.project.controllers.models.UserOutputModel
import pt.isel.daw.g08.project.controllers.models.UsersOutputModel
import pt.isel.daw.g08.project.database.helpers.UsersDb
import pt.isel.daw.g08.project.pipeline.argumentresolvers.Pagination
import pt.isel.daw.g08.project.responses.Response
import pt.isel.daw.g08.project.responses.siren.SirenLink
import java.net.URI

@RestController
@RequestMapping(USERS_HREF)
class UsersController(val db: UsersDb) : Routes() {

    @GetMapping
    fun getAllUsers(
        pagination: Pagination
    ): ResponseEntity<Response> {
        val usersDao = db.getAllUsers(pagination.page, pagination.limit)
        val collectionSize = db.getUsersCount()
        val users = UsersOutputModel(
            collectionSize = collectionSize,
            pageIndex = pagination.page,
            pageSize = usersDao.size
        )
        val usersUri = "${env.getBaseUrl()}/${USERS_HREF}"

        return createResponseEntity(
            users.toSirenObject(
                entities = usersDao.map { userDao ->
                    UserOutputModel(
                        id = userDao.uid,
                        name = userDao.username
                    ).toSirenObject(
                        rel = listOf("item"),
                        links = listOf(
                            SirenLink(rel = listOf("self"), href = URI("${usersUri}/${userDao.uid}")),
                            SirenLink(rel = listOf("users"), href = URI(usersUri))
                        ),
                    )
                },
                links = createUriListForPagination(usersUri, pagination.page, users.pageSize, pagination.limit, collectionSize)
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("{userId}")
    fun getUser(
        @PathVariable userId: Int,
    ): ResponseEntity<Response> {
        //TODO: Exceptions (404 when not found)
        val userDao = db.getUserById(userId)
        val user = UserOutputModel(
            id = userDao.uid,
            name = userDao.username
        )
        val selfUri = URI("${env.getBaseUrl()}/${USERS_HREF}/${user.id}")
        val usersUri = URI("${env.getBaseUrl()}/${USERS_HREF}")

        return createResponseEntity(
            user.toSirenObject(
                links = listOf(
                    SirenLink(rel = listOf("self"), href = selfUri),
                    SirenLink(rel = listOf("users"), href = usersUri)
                ),
            ),
            HttpStatus.OK
        )
    }
}