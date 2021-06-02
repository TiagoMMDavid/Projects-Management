import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { CommentItem } from './CommentItem'
import { CreateComment } from './CreateComment'
import { getSirenAction } from '../../api/apiRoutes'

type CommentsGetter = (projectId: number, issueNumber: number, page: number, credentials: Credentials) => Promise<IssueComments>

type CommentsPageProps = {
    getComments: CommentsGetter
}

type CommentsPageParams = {
    projectId: string,
    issueNumber: string
}

type State = {
    state: 'has-comments' | 'loading-comments' | 'page-reset' | 'hide' | 'message'
    comments: IssueComments
    message: string
}
  
type Action =
    { type: 'set-loading', message: string } |
    { type: 'set-comments', comments: IssueComments, message: string } |
    { type: 'reset-page', message: string} |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-comments', comments: null, message: action.message } as State
        case 'set-comments': return { state: 'has-comments', comments: action.comments, message: action.message } as State
        case 'reset-page': return { state: 'page-reset', comments: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function CommentsPage({ getComments }: CommentsPageProps): JSX.Element {
    const { projectId, issueNumber } = useParams<CommentsPageParams>()

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, comments, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'} as Action)
        getComments(Number(projectId), Number(issueNumber), page, ctx.credentials)
            .then(comments => {
                if (!comments) dispatch({ type: 'set-message', message: 'Failed to get comments' } as Action)
                else dispatch({ type: 'set-comments', comments: comments, message: message})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let commentsView: JSX.Element
    switch(state) {
        case 'message':
            commentsView = <h1>{message}</h1>
            break
        case 'hide':
            break
        case 'page-reset':
        case 'loading-comments':
            commentsView = <h1>Loading comments...</h1>
            break
        case 'has-comments':
            commentsView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(comments.actions, 'create-comment') != null ?
                        <CreateComment
                            onFinishCreating={(success, message) => dispatch({ type: 'reset-page', message: message } as Action)}
                            onCreating={() => dispatch({type: 'hide'})}
                            credentials={ctx.credentials} 
                            projectId={Number(projectId)}
                            issueNumber={Number(issueNumber)}
                        />
                        : <> </>
                    }
                    <Paginated onChangePage={getPage} isLastPage={comments.isLastPage} page={comments.page}>
                        <h1>Comments</h1>
                        { comments.comments.length == 0 ? <p> No comments found </p> :
                            comments.comments.map((comment: IssueComment) => <CommentItem key={comment.id} comment={comment} />)}
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}/issues/${issueNumber}`}>Back to issue</Link>
            { commentsView }
        </div>
    )
}

export {
    CommentsPage
}