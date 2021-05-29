import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions } from '../api/apiRoutes'

const USERS_LIMIT = 10

function getAuthUser(credentials: Credentials): Promise<User> {
    return fetch(
        apiRoutes.user.getAuthenticatedUserRoute.href, 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(entity => {
            if (!entity) return null

            return {
                id: entity.properties.id,
                name: entity.properties.name
            } as User
        })
}

function getUser(userId: number, credentials: Credentials): Promise<User> {
    return fetch(
        apiRoutes.user.getUserRoute.hrefTemplate.expandUriTemplate(userId), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(entity => {
            if (!entity) return null

            return {
                id: entity.properties.id,
                name: entity.properties.name
            } as User
        })
}

function validateUser(credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.user.getAuthenticatedUserRoute.href, 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status == 200)
}

export {
    getAuthUser,
    getUser,
    validateUser
}