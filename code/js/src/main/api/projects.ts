import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions, throwErrorFromResponse } from '../api/apiRoutes'

const PROJECTS_LIMIT = 10

function getProjects(page: number, credentials: Credentials): Promise<Projects> {
    return fetch(
        apiRoutes.project.getProjectsRoute.href.toPaginatedUri(page, PROJECTS_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting projects') : res.json())
        .then(collection => {
            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const projects = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    name: entity.properties.name,
                    description: entity.properties.description,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as Project
            })

            return {
                projects: projects,
                page: page,
                isLastPage: PROJECTS_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as Projects
        })
}

function getProject(projectId: number, credentials: Credentials): Promise<Project> {
    return fetch(
        apiRoutes.project.getProjectRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting project') : res.json())
        .then(entity => {
            return {
                id: entity.properties.id,
                name: entity.properties.name,
                description: entity.properties.description,
                author: entity.properties.author,
                authorId: entity.properties.authorId,
            
                links: entity.links,
                actions: entity.actions
            } as Project
        })
}

function createProject(name: string, description: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.project.getProjectsRoute.href, 
        getRequestOptions('POST', credentials, {
            name: name,
            description: description
        })
    )
        .then(res => { if (res.status != 201) return throwErrorFromResponse(res, 'Error while creating project') })
}

function editProject(projectId: number, name: string, description: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.project.getProjectRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('PUT', credentials, {
            name: name,
            description: description
        })
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while editing project') })
}

function deleteProject(projectId: number, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.project.getProjectRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('DELETE', credentials)
    )   
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while deleting project') })
}

export {
    getProjects,
    getProject,
    createProject,
    editProject,
    deleteProject
}