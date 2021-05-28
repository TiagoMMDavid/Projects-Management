import { Credentials, getUserCredentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions } from '../api/apiRoutes'

function getAuthUser(credentials: Credentials): string {
    return null
}

function validateUser(credentials: Credentials): Promise<boolean> {
    // TODO: Maybe throw error if status is not expected (e.g: 500 Internal Server Error)
    return fetch(
        apiRoutes.user.getAuthenticatedUserRoute.href, 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status == 200)
}

export {
    validateUser
}