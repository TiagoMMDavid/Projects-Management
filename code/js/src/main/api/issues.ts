import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions, throwErrorFromResponse } from '../api/apiRoutes'

const ISSUES_LIMIT = 10

function getIssues(projectId: number, page: number, credentials: Credentials): Promise<Issues> {
    return fetch(
        apiRoutes.issue.getIssuesRoute.hrefTemplate.expandUriTemplate(projectId).toPaginatedUri(page, ISSUES_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting issues') : res.json())
        .then(collection => {
            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const issues = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    number: entity.properties.number,
                    name: entity.properties.name,
                    description: entity.properties.description,
                    createDate: entity.properties.createDate,
                    closeDate: entity.properties.closeDate,
                    state: entity.properties.state,
                    stateNumber: entity.properties.stateNumber,
                    project: entity.properties.project,
                    projectId: entity.properties.projectId,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as Issue
            })

            return {
                issues: issues,
                page: page,
                isLastPage: ISSUES_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as Issues
        })
}

function getIssue(projectId: number, issueNumber: number, credentials: Credentials): Promise<Issue> {
    return fetch(
        apiRoutes.issue.getIssueRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting issue') : res.json())
        .then(entity => {
            return {
                id: entity.properties.id,
                number: entity.properties.number,
                name: entity.properties.name,
                description: entity.properties.description,
                createDate: entity.properties.createDate,
                closeDate: entity.properties.closeDate,
                state: entity.properties.state,
                stateNumber: entity.properties.stateNumber,
                project: entity.properties.project,
                projectId: entity.properties.projectId,
                author: entity.properties.author,
                authorId: entity.properties.authorId,

                links: entity.links,
                actions: entity.actions
            } as Issue
        })
}

function createIssue(projectId: number, name: string, description: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.issue.getIssuesRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('POST', credentials, {
            name: name,
            description: description
        })
    )
        .then(res => { if (res.status != 201) return throwErrorFromResponse(res, 'Error while creating issue') })
}

function editIssue(projectId: number, issueNumber: number, name: string, description: string, state: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.issue.getIssueRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber), 
        getRequestOptions('PUT', credentials, {
            name: name,
            description: description,
            state: state
        })
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while editing issue') })
}

function deleteIssue(projectId: number, issueNumber: number, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.issue.getIssueRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while deleting issue') })
}

export {
    getIssues,
    getIssue,
    createIssue,
    editIssue,
    deleteIssue
}