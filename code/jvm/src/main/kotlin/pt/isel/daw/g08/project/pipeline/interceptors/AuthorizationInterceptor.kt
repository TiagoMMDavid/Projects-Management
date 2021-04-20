package pt.isel.daw.g08.project.pipeline.interceptors

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.daw.g08.project.auth.AuthHeaderValidator
import pt.isel.daw.g08.project.auth.AuthHeaderValidator.AUTH_HEADER
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.database.helpers.UsersDb
import pt.isel.daw.g08.project.exceptions.AuthorizationException
import java.lang.Exception
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth

const val USER_ATTRIBUTE = "user-attribute"

@Component
class AuthorizationInterceptor(val db: UsersDb) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.java)
    private val digester = MessageDigest.getInstance("SHA-256")

    fun getAndVerifyUser(username: String, password: String): User? {
        val userDao = db.getUserByUsername(username)
        val hashedPass = digester.digest(password.toByteArray())
            .fold("") { str, byte -> "$str${"%02x".format(byte)}" }

        return if (userDao.pass == hashedPass) userDao else null
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val routeHandler = handler as? HandlerMethod

        if (routeHandler == null || !routeHandler.hasMethodAnnotation(RequiresAuth::class.java)) {
            return true
        }

        // Handler has 'RequiresAuth' annotation. We need to decode the auth header into username:pwd
        val authorizationHeader = request.getHeader(AUTH_HEADER)?.trim()
        logger.info("PreHandle with handler ${handler.javaClass.name} requires authentication")

        val user = AuthHeaderValidator.validate(authorizationHeader, ::getAndVerifyUser)

        // User is valid
        request.setAttribute(USER_ATTRIBUTE, user)
        logger.info("User with name ${user.username} is valid")
        return true
    }
}