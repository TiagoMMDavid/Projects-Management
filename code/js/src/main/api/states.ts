import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions, throwErrorFromResponse } from '../api/apiRoutes'

const STATES_LIMIT = 10
const MAX_NEXT_STATES = 50

function getProjectStates(projectId: number, page: number, credentials: Credentials): Promise<IssueStates> {
    return getStates(
        fetch(
            apiRoutes.state.getStatesRoute.hrefTemplate.expandUriTemplate(projectId).toPaginatedUri(page, STATES_LIMIT), 
            getRequestOptions('GET', credentials)
        )
    )
}

function searchStates(projectId: number, searchName: string, excludeStateId: number, credentials: Credentials): Promise<IssueStates> {
    let stateParam
    if (excludeStateId == null) stateParam = ''
    else stateParam = `${searchName != null ? '&' : ''}excludeState=${excludeStateId}`

    const uri = `${apiRoutes.state.getStatesRoute.hrefTemplate.expandUriTemplate(projectId)}` +
                `?${searchName != null ? `q=${searchName}` : ''}` +
                stateParam

    return getStates(
        fetch(uri, getRequestOptions('GET', credentials))
    )
}

function getStates(promise: Promise<Response>, page = 0): Promise<IssueStates> {
    return promise
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting states') : res.json())
        .then(collection => {
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

function getNextStates(projectId: number, stateNumber: number, credentials: Credentials, page: number = null): Promise<IssueStates> {
    const baseUri = apiRoutes.state.getNextStatesRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber)
    const uri = page == null ? baseUri.toPaginatedUri(0, MAX_NEXT_STATES) : baseUri.toPaginatedUri(page, STATES_LIMIT)

    return fetch(
        uri, 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting next states') : res.json())
        .then(collection => {
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
                isLastPage: page == null ? null : STATES_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as IssueStates
        })
}

function getState(projectId: number, stateNumber: number, credentials: Credentials): Promise<IssueState> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting state') : res.json())
        .then(entity => {
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

function createState(projectId: number, name: string, isStartState: boolean, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.state.getStatesRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('POST', credentials, {
            name: name,
            isStart: isStartState
        })
    )
        .then(res => { if (res.status != 201) return throwErrorFromResponse(res, 'Error while creating state') })
}

function addNextState(projectId: number, stateNumber: number, nextStateNumber: number, credentials: Credentials): Promise<void> {
    return fetch(
        `${apiRoutes.state.getNextStatesRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber)}/${nextStateNumber}`,
        getRequestOptions('PUT', credentials)
    )
        .then(res => { if (res.status != 201) return throwErrorFromResponse(res, 'Error while adding next state') })
}

function editState(projectId: number, stateNumber: number, name: string, isStartState: boolean, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('PUT', credentials, {
            name: name,
            isStartState: isStartState
        })
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while editing state') })
}

function deleteState(projectId: number, stateNumber: number, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.state.getStateRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while deleting state') })
}

function deleteNextState(projectId: number, stateNumber: number, nextStateNumber: number, credentials: Credentials): Promise<void> {
    return fetch(
        `${apiRoutes.state.getNextStatesRoute.hrefTemplate.expandUriTemplate(projectId, stateNumber)}/${nextStateNumber}`,
        getRequestOptions('DELETE', credentials)
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while deleting next state') })
}

export {
    getProjectStates,
    searchStates,
    getNextStates,
    getState,
    createState,
    addNextState,
    editState,
    deleteState,
    deleteNextState
}