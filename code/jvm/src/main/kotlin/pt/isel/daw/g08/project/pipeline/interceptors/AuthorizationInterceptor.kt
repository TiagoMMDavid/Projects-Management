package pt.isel.daw.g08.project.pipeline.interceptors

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.daw.g08.project.database.dao.UserDao
import pt.isel.daw.g08.project.database.helpers.UsersDb
import java.security.MessageDigest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth

const val BASIC_SCHEME = "Basic"
const val AUTH_HEADER = "Authorization"

const val USER_ATTRIBUTE = "user-attribute"

@Component
class AuthorizationInterceptor(val db: UsersDb) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(AuthorizationInterceptor::class.java)
    private val digester = MessageDigest.getInstance("SHA-256")

    fun getAndVerifyUser(username: String, password: String): UserDao? {
        // TODO: If user doesn't exist, exception
        val userDao = db.getUserByUsername(username)
        val hashedPass = digester.digest(password.toByteArray())
            .fold("", { str, byte -> "$str${"%02x".format(byte)}" })

        return if (userDao.pass == hashedPass) userDao else null
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val routeHandler = handler as? HandlerMethod

        if (routeHandler?.hasMethodAnnotation(RequiresAuth::class.java) == true) {
            // username:pwd
            logger.info("AuthorizationInterceptor - PreHandle with handler ${handler.javaClass.name} requires authentication")
            val authorizationHeader = request.getHeader(AUTH_HEADER)?.trim()

            if (authorizationHeader != null && authorizationHeader.startsWith(BASIC_SCHEME, true)) {
                val credentials = authorizationHeader.drop(BASIC_SCHEME.length + 1).trim()
                val (username, password) = String(Base64Utils.decodeFromString(credentials)).split(':')

                val userDao = getAndVerifyUser(username, password)
                if (userDao != null) {
                    request.setAttribute(USER_ATTRIBUTE, userDao)
                    logger.info("AuthorizationInterceptor - User with name ${username} is valid")
                    return true
                } else {
                    logger.info("AuthorizationInterceptor - User with name ${username} is invalid")
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    return false
                }
            } else {
                logger.info("AuthorizationInterceptor - Authorization header was not provided")
                response.status = HttpServletResponse.SC_FORBIDDEN
                // TODO: Problem JSON
                return false
            }
        } else {
            return true
        }
    }
}