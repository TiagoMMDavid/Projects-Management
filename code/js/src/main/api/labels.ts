import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions } from '../api/apiRoutes'

const LABELS_LIMIT = 10

function getProjectLabels(projectId: number, page: number, credentials: Credentials): Promise<Labels> {
    return getLabels(
        fetch(
            apiRoutes.label.getLabelsRoute.hrefTemplate.expandUriTemplate(projectId).toPaginatedUri(page, LABELS_LIMIT), 
            getRequestOptions('GET', credentials)
        ), page
    )
}

function searchLabels(projectId: number, searchName: string, excludeIssueId: number, credentials: Credentials): Promise<Labels> {
    let issueParam
    if (excludeIssueId == null) issueParam = ''
    else issueParam = `${searchName != null ? '&' : ''}excludeIssue=${excludeIssueId}`

    const uri = `${apiRoutes.label.getLabelsRoute.hrefTemplate.expandUriTemplate(projectId)}` +
                `?${searchName != null ? `q=${searchName}` : ''}` +
                issueParam

    return getLabels(
        fetch(uri, getRequestOptions('GET', credentials))
    )
}

function getIssueLabels(projectId: number, issueNumber: number, page: number, credentials: Credentials): Promise<Labels> {
    return getLabels(
        fetch(
            apiRoutes.label.getIssueLabelsRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber).toPaginatedUri(page, LABELS_LIMIT), 
            getRequestOptions('GET', credentials)
        ), page
    )
}

function getLabels(promise: Promise<Response>, page = 0): Promise<Labels> {
    return promise
        .then(res => res.status != 200 ? null : res.json())
        .then(collection => {

            if (!collection) return null

            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const labels = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    number: entity.properties.number,
                    name: entity.properties.name,
                    project: entity.properties.project,
                    projectId: entity.properties.projectId,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as Label
            })

            return {
                labels: labels,
                page: page,
                isLastPage: LABELS_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as Labels
        })
}

function getLabel(projectId: number, labelNumber: number, credentials: Credentials): Promise<Label> {
    return fetch(
        apiRoutes.label.getLabelRoute.hrefTemplate.expandUriTemplate(projectId, labelNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? null : res.json())
        .then(entity => {
            if (!entity) return null

            return {
                id: entity.properties.id,
                number: entity.properties.number,
                name: entity.properties.name,
                project: entity.properties.project,
                projectId: entity.properties.projectId,
                author: entity.properties.author,
                authorId: entity.properties.authorId,

                links: entity.links,
                actions: entity.actions
            } as Label
        })
}

function createLabel(projectId: number, name: string, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.label.getLabelsRoute.hrefTemplate.expandUriTemplate(projectId), 
        getRequestOptions('POST', credentials, {
            name: name
        })
    )
        .then(res => res.status == 201)
}

function editLabel(projectId: number, labelNumber: number, name: string, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.label.getLabelRoute.hrefTemplate.expandUriTemplate(projectId, labelNumber), 
        getRequestOptions('PUT', credentials, {
            name: name
        })
    )
        .then(res => res.status == 200)
}

function deleteLabel(projectId: number, labelNumber: number, credentials: Credentials): Promise<boolean> {
    return fetch(
        apiRoutes.label.getLabelRoute.hrefTemplate.expandUriTemplate(projectId, labelNumber), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => res.status == 200)
}

function addLabelToIssue(projectId: number, issueNumber: number, labelNumber: number, credentials: Credentials): Promise<boolean> {
    return fetch(
        `${apiRoutes.label.getIssueLabelsRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber)}/${labelNumber}`,
        getRequestOptions('PUT', credentials)
    )
        .then(res => res.status == 201)
}


function removeLabelFromIssue(projectId: number, issueNumber: number, labelNumber: number, credentials: Credentials): Promise<boolean> {
    return fetch(
        `${apiRoutes.label.getIssueLabelsRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber)}/${labelNumber}`,
        getRequestOptions('DELETE', credentials)
    )
        .then(res => res.status == 200)
}

export {
    getProjectLabels,
    getIssueLabels,
    searchLabels,
    getLabel,
    createLabel,
    editLabel,
    deleteLabel,
    addLabelToIssue,
    removeLabelFromIssue
}