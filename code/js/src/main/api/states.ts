import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions } from '../api/apiRoutes'

const STATES_LIMIT = 10

function getStates(projectId: number, page: number, credentials: Credentials): Promise<IssueStates> {
    return fetch(
        apiRoutes.state.getStatesRoute.hrefTemplate.expandUriTemplate(projectId).toPaginatedUri(page, STATES_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(collection => {
            if (!collection) return null

            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const states = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    number: entity.properties.number,
                    name: entity.properties.name,
                    isStartState: entity.properties.isStartState,
                    project: entity.properties.project,
                    projectId: entity.properties.projectId,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as IssueState
            })

            return {
                states: states,
                page: page,
                isLastPage: STATES_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as IssueStates
        })
}

function getNextStates(projectId: number, stateNumber: number, credentials: Credentials): Promise<NextStates> {
    return fetch(
        apiRoutes.state.getNextStatesRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(collection => {
            if (!collection) return null

            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const states = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    number: entity.properties.number,
                    name: entity.properties.name,
                    isStartState: entity.properties.isStartState,
                    project: entity.properties.project,
                    projectId: entity.properties.projectId,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as IssueState
            })

            return {
                states: states,
            
                links: links,
                actions: actions,
            } as NextStates
        })
}

function getState(projectId: number, stateNumber: number, credentials: Credentials): Promise<IssueState> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(entity => {
            if (!entity) return null

            return {
                id: entity.properties.id,
                number: entity.properties.number,
                name: entity.properties.name,
                isStartState: entity.properties.isStartState,
                project: entity.properties.project,
                projectId: entity.properties.projectId,
                author: entity.properties.author,
                authorId: entity.properties.authorId,

                links: entity.links,
                actions: entity.actions
            } as IssueState
        })
}

function createState(projectId: number, name: string, isStartState: boolean, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.state.getStatesRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('POST', credentials, {
            name: name,
            isStart: isStartState
        })
    )
        .then(res => res.status == 201)
}

function editState(projectId: number, stateNumber: number, name: string, isStartState: boolean, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('PUT', credentials, {
            name: name,
            isStartState: isStartState
        })
    )
        .then(res => res.status == 200)
}

function deleteState(projectId: number, stateNumber: number, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => res.status == 200)
}

export {
    getStates,
    getNextStates,
    getState,
    createState,
    editState,
    deleteState
}