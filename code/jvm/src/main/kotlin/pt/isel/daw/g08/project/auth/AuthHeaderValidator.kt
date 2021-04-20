package pt.isel.daw.g08.project.auth

import org.slf4j.LoggerFactory
import org.springframework.util.Base64Utils
import pt.isel.daw.g08.project.database.dto.User
import pt.isel.daw.g08.project.exceptions.AuthorizationException

object AuthHeaderValidator {
    const val AUTH_SCHEME = "Basic"
    const val AUTH_HEADER = "Authorization"

    private val logger = LoggerFactory.getLogger(AuthHeaderValidator::class.java)

    fun validate(header: String?, validateUserFunction: (username: String, password: String) -> User?): User {
        if (header == null) {
            logger.info("Authorization header was not provided")
            throw AuthorizationException("Resource requires authentication")
        }
        if (!header.startsWith(AUTH_SCHEME, true)) {
            logger.info("Authorization header didn't follow the auth basic scheme")
            throw AuthorizationException("Invalid authorization scheme")
        }

        // Get user credentials
        try {
            val credentials = header.drop(AUTH_SCHEME.length + 1).trim()
            val (username, password) = String(Base64Utils.decodeFromString(credentials)).split(':')

            val user = validateUserFunction(username, password)
            if (user == null) {
                // User failed the verification (wrong password)
                logger.info("User with name $username is invalid")
                throw AuthorizationException("Bad credentials")
            }

            return user
        } catch(ex: Exception) {
            logger.info("Could not get user credentials")
            throw AuthorizationException("Bad credentials")
        }
    }
}