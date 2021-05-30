import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions } from '../api/apiRoutes'

const PROJECTS_LIMIT = 10

function getProjects(page: number, credentials: Credentials): Promise<Projects> {
    return fetch(
        apiRoutes.project.getProjectsRoute.href.toPaginatedUri(page, PROJECTS_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(collection => {
            if (!collection) return null

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
        .then(res => res.status != 200 ? null : res.json())
        .then(entity => {
            if (!entity) return null

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

function createProject(name: string, description: string, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.project.getProjectsRoute.href, 
        getRequestOptions('POST', credentials, {
            name: name,
            description: description
        })
    )
        .then(res => res.status == 201)
}

function editProject(projectId: number, name: string, description: string, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.project.getProjectRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('PUT', credentials, {
            name: name,
            description: description
        })
    )
        .then(res => res.status == 200)
}

function deleteProject(projectId: number, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.project.getProjectRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => res.status == 200)
}

export {
    getProjects,
    getProject,
    createProject,
    editProject,
    deleteProject
}