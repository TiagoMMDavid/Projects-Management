import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import { DeleteComment } from './DeleteComment'
import { EditComment } from './EditComment'

type CommentPageProps = {
    getComment: (projectId: number, issueNumber: number, commentNumber: number, credentials: Credentials) => Promise<IssueComment>
}
type CommentProps = {
    comment: IssueComment
}

type CommentPageParams = {
    projectId: string,
    issueNumber: string,
    commentNumber: string
}

function Comment({ comment }: CommentProps): JSX.Element {
    return (
        <div>
            <p>Number: {comment.number}</p>
            <p>Content: {comment.content}</p>
            <p>Date: {new Date(comment.createDate).toLocaleString()}</p>
            <p>Author: <Link to={`/users/${comment.authorId}`}>{comment.author}</Link></p>
        </div>
    )
}

type State = {
    state: 'has-comment' | 'loading-comment' | 'deleted-comment' | 'edited-comment' | 'message'
    message: string
    comment: IssueComment
}
  
type Action =
    { type: 'set-comment', comment: IssueComment, message: string } |
    { type: 'loading-comment', message: string } |
    { type: 'set-deleted-comment' } |
    { type: 'set-edited-comment' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-comment': return { state: 'has-comment', comment: action.comment, message: action.message } as State
        case 'loading-comment': return { state: 'loading-comment', message: action.message } as State
        case 'set-deleted-comment': return { state: 'deleted-comment' } as State
        case 'set-edited-comment': return { state: 'edited-comment' } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function CommentPage({ getComment }: CommentPageProps): JSX.Element {
    const { projectId, issueNumber, commentNumber } = useParams<CommentPageParams>()
    const [{ state, comment, message }, dispatch] = useReducer(reducer, {state: 'loading-comment'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-comment' || state == 'loading-comment') {
            getComment(Number(projectId), Number(issueNumber), Number(commentNumber), ctx.credentials)
                .then(comment => {
                    if (comment) dispatch({ type: 'set-comment', comment: comment, message: message })
                    else dispatch({ type: 'set-message', message: 'Comment Not Found' })
                })
        }
    }, [projectId, commentNumber, state])

    if (state == 'deleted-comment')
        return (
            <Redirect to={`/projects/${projectId}/issues/${issueNumber}/comments`} />
        )

    let body: JSX.Element
    switch(state) {
        case 'message':
            body = (
                <h1>{message}</h1>
            )
            break
        case 'edited-comment':
        case 'loading-comment':
            body = (
                <h1>Loading comment...</h1> 
            )
            break
        case 'has-comment':
            body = (
                <div>
                    <h4>{message}</h4>
                    <EditComment
                        comment={comment}
                        onFinishEdit={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-edited-comment' })
                            } else {
                                dispatch({ type: 'loading-comment', message: message })
                            }
                        }}
                        onEdit={() => dispatch({ type: 'set-message', message: 'Editing Comment...' })}
                        credentials={ctx.credentials} 
                    />
                    <DeleteComment
                        comment={comment}
                        onFinishDelete={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-deleted-comment' })
                            } else {
                                dispatch({ type: 'loading-comment', message: message })
                            }
                        }}
                        onDelete={() => dispatch({ type: 'set-message', message: 'Deleting Comment...' })}
                        credentials={ctx.credentials}
                    />
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/comments`}>View all comments</Link>
            {body}
            {comment == null ? <></> :  <Comment comment={comment}/>}
        </div>
    )
}

export {
    CommentPage
}