package pt.isel.daw.g08.project.pipeline.interceptors

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.daw.g08.project.database.dao.UserDao
import pt.isel.daw.g08.project.database.helpers.UsersDb
import pt.isel.daw.g08.project.exceptions.AuthorizationException
import java.lang.Exception
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth

private const val AUTH_SCHEME = "Basic"
private const val AUTH_HEADER = "Authorization"

const val USER_ATTRIBUTE = "user-attribute"

@Component
class AuthorizationInterceptor(val db: UsersDb) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.java)
    private val digester = MessageDigest.getInstance("SHA-256")

    fun getAndVerifyUser(username: String, password: String): UserDao? {
        val userDao = db.getUserByUsername(username)
        val hashedPass = digester.digest(password.toByteArray())
            .fold("", { str, byte -> "$str${"%02x".format(byte)}" })

        return if (userDao.pass == hashedPass) userDao else null
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val routeHandler = handler as? HandlerMethod

        if (routeHandler == null || !routeHandler.hasMethodAnnotation(RequiresAuth::class.java)) {
            return true
        }

        // Handler has 'RequiresAuth' annotation. We need to decode the auth header into username:pwd
        logger.info("AuthorizationInterceptor - PreHandle with handler ${handler.javaClass.name} requires authentication")
        val authorizationHeader = request.getHeader(AUTH_HEADER)?.trim()
        if (authorizationHeader == null) {
            logger.info("AuthorizationInterceptor - Authorization header was not provided")
            throw AuthorizationException("Resource requires authentication")
        }
        if (!authorizationHeader.startsWith(AUTH_SCHEME, true)) {
            logger.info("AuthorizationInterceptor - Authorization header didn't follow the auth basic scheme")
            throw AuthorizationException("Invalid authorization scheme")
        }

        // Get user credentials
        try {
            val credentials = authorizationHeader.drop(AUTH_SCHEME.length + 1).trim()
            val (username, password) = String(Base64Utils.decodeFromString(credentials)).split(':')

            val userDao = getAndVerifyUser(username, password)
            if (userDao == null) {
                // User failed the verification (wrong password)
                logger.info("AuthorizationInterceptor - User with name $username is invalid")
                throw AuthorizationException("Bad credentials")
            }

            // User is valid
            request.setAttribute(USER_ATTRIBUTE, userDao)
            logger.info("AuthorizationInterceptor - User with name $username is valid")
            return true
        } catch(ex: Exception) {
            logger.info("AuthorizationInterceptor - Could not get user credentials")
            throw AuthorizationException("Bad credentials")
        }
    }
}