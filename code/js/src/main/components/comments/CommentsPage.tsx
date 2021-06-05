import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { CommentItem } from './CommentItem'
import { CreateComment } from './CreateComment'
import { getSirenAction } from '../../api/apiRoutes'
import { getComments } from '../../api/comments'

type CommentsPageParams = {
    projectId: string,
    issueNumber: string
}

type State = {
    state: 'has-comments' | 'page-set' | 'message'
    comments: IssueComments
    message: string
}
  
type Action =
    { type: 'set-comments', comments: IssueComments, message: string } |
    { type: 'set-page', message: string } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-comments': return { state: 'has-comments', comments: action.comments, message: action.message } as State
        case 'set-page': return { state: 'page-set', comments: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function CommentsPage(): JSX.Element {
    const { projectId, issueNumber } = useParams<CommentsPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, comments, message }, dispatch] = useReducer(reducer, { state: 'page-set' } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getComments(Number(projectId), Number(issueNumber), page, ctx.credentials)
                .then(comments => {
                    if (isCancelled) return
                    dispatch({ type: 'set-comments', comments: comments, message: message})
                })
                .catch(err => {
                    if (isCancelled) return
                    dispatch({ type: 'set-message', message: err.message })
                })
        }

        return () => {
            isCancelled = true
        }
    }, [state])

    let commentsView: JSX.Element
    switch(state) {
        case 'message':
            commentsView = <h1>{message}</h1>
            break
        case 'page-set':
            commentsView = <h1>Loading comments...</h1>
            break
        case 'has-comments':
            commentsView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(comments.actions, 'create-comment') != null ?
                        <CreateComment
                            onFinishCreating={(success, message) => dispatch({ type: 'set-page', message: message, page: page } as Action)}
                            credentials={ctx.credentials} 
                            projectId={Number(projectId)}
                            issueNumber={Number(issueNumber)}
                        />
                        : <> </>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={comments.isLastPage} page={comments.page}>
                        <h1>Comments</h1>
                        { comments.comments.length == 0 ? 
                            <p> No comments found </p> :
                            <ul>
                                {comments.comments.map((comment: IssueComment) => <CommentItem key={comment.id} comment={comment} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}/issues/${issueNumber}`}>{'<< Back to issue'}</Link>
            { commentsView }
        </div>
    )
}

export {
    CommentsPage
}