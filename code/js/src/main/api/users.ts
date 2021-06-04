import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions, throwErrorFromResponse } from '../api/apiRoutes'

const USERS_LIMIT = 10

function getUsers(page: number, credentials: Credentials): Promise<Users> {
    return fetch(
        apiRoutes.user.getUsersRoute.href.toPaginatedUri(page, USERS_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting users') : res.json())
        .then(collection => {
            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const users = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    name: entity.properties.name,

                    links: links,
                    actions: actions
                } as User
            })

            return {
                users: users,
                page: page,
                isLastPage: USERS_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as Users
        })
}


function getUser(userId: number, credentials: Credentials): Promise<User> {
    return fetch(
        apiRoutes.user.getUserRoute.hrefTemplate.expandUriTemplate(userId), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting user') : res.json())
        .then(entity => {
            return {
                id: entity.properties.id,
                name: entity.properties.name
            } as User
        })
}

function getAuthUser(credentials: Credentials): Promise<User> {
    return fetch(
        apiRoutes.user.getAuthenticatedUserRoute.href, 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting user') : res.json())
        .then(entity => {
            return {
                id: entity.properties.id,
                name: entity.properties.name
            } as User
        })
}

export {
    getUsers,
    getAuthUser,
    getUser,
}