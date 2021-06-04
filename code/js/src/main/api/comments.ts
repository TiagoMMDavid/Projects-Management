import { Credentials } from '../utils/userSession'
import { apiRoutes, getRequestOptions, throwErrorFromResponse } from '../api/apiRoutes'

const COMMENTS_LIMIT = 10

function getComments(projectId: number, issueNumber: number, page: number, credentials: Credentials): Promise<IssueComments> {
    return fetch(
        apiRoutes.comment.getCommentsRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber).toPaginatedUri(page, COMMENTS_LIMIT), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting comments') : res.json())
        .then(collection => {
            const entities = Array.from(collection.entities) as any[]
            const actions: SirenAction[] = Array.from(collection.actions || [])
            const links: SirenLink[] = Array.from(collection.links)

            const comments = entities.map(entity => {
                const actions: SirenAction[] = Array.from(entity.actions || [])
                const links: SirenLink[] = Array.from(entity.links)

                return {
                    id: entity.properties.id,
                    number: entity.properties.number,
                    content: entity.properties.content,
                    createDate: entity.properties.createDate,
                    issue: entity.properties.issue,
                    issueNumber: entity.properties.issueNumber,
                    project: entity.properties.project,
                    projectId: entity.properties.projectId,
                    author: entity.properties.author,
                    authorId: entity.properties.authorId,

                    links: links,
                    actions: actions
                } as IssueComment
            })

            return {
                comments: comments,
                page: page,
                isLastPage: COMMENTS_LIMIT * (collection.properties.pageIndex + 1) >= collection.properties.collectionSize,
            
                links: links,
                actions: actions,
            } as IssueComments
        })
}

function getComment(projectId: number, issueNumber: number, commentNumber: number, credentials: Credentials): Promise<IssueComment> {
    return fetch(
        apiRoutes.comment.getCommentRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber, commentNumber), 
        getRequestOptions('GET', credentials)
    )
        .then(res => res.status != 200 ? throwErrorFromResponse(res, 'Error while getting comment') : res.json())
        .then(entity => {
            return {
                id: entity.properties.id,
                number: entity.properties.number,
                content: entity.properties.content,
                createDate: entity.properties.createDate,
                issue: entity.properties.issue,
                issueNumber: entity.properties.issueNumber,
                project: entity.properties.project,
                projectId: entity.properties.projectId,
                author: entity.properties.author,
                authorId: entity.properties.authorId,

                links: entity.links,
                actions: entity.actions
            } as IssueComment
        })
}

function createComment(projectId: number, issueNumber: number, content: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.comment.getCommentsRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber), 
        getRequestOptions('POST', credentials, {
            content: content
        })
    )
        .then(res => { if (res.status != 201) return throwErrorFromResponse(res, 'Error while creating comment') })
}

function editComment(projectId: number, issueNumber: number, commentNumber: number, content: string, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.comment.getCommentRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber, commentNumber), 
        getRequestOptions('PUT', credentials, {
            content: content
        })
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while editing comment') })
}

function deleteComment(projectId: number, issueNumber: number, commentNumber: number, credentials: Credentials): Promise<void> {
    return fetch(
        apiRoutes.comment.getCommentRoute.hrefTemplate.expandUriTemplate(projectId, issueNumber, commentNumber), 
        getRequestOptions('DELETE', credentials)
    )
        .then(res => { if (res.status != 200) return throwErrorFromResponse(res, 'Error while deleting comment') })
}

export {
    getComments,
    getComment,
    createComment,
    editComment,
    deleteComment
}