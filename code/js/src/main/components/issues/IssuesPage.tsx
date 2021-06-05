import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { IssueItem } from './IssueItem'
import { CreateIssue } from './CreateIssue'
import { getIssues } from '../../api/issues'
import { getSirenAction } from '../../api/apiRoutes'

type IssuesPageParams = {
    projectId: string
}

type State = {
    state: 'has-issues' | 'page-set' |  'message'
    issues: Issues
    message: string
}
  
type Action =
    { type: 'set-issues', issues: Issues, message: string } |
    { type: 'set-page', message: string } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-issues': return { state: 'has-issues', issues: action.issues, message: action.message } as State
        case 'set-page': return { state: 'page-set', issues: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function IssuesPage(): JSX.Element {
    const { projectId } = useParams<IssuesPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, issues, message }, dispatch] = useReducer(reducer, { state: 'page-set' } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getIssues(Number(projectId), page, ctx.credentials)
                .then(issues => {
                    if (isCancelled) return
                    dispatch({ type: 'set-issues', issues: issues, message: message})
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

    let issuesView: JSX.Element
    switch(state) {
        case 'message':
            issuesView = <h1>{message}</h1>
            break
        case 'page-set':
            issuesView = <h1>Loading issues...</h1>
            break
        case 'has-issues':
            issuesView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(issues.actions, 'create-issue') != null ?
                        <CreateIssue
                            onFinishCreating={(success, message) => dispatch({ type: 'set-page', message: message })} 
                            credentials={ctx.credentials} 
                            projectId={Number(projectId)}
                        />
                        : <> </>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={issues.isLastPage} page={issues.page}>
                        <h1>Issues</h1>
                        { issues.issues.length == 0 ? 
                            <p> No issues found </p> :
                            <ul>
                                {issues.issues.map((issue: Issue) => <IssueItem key={issue.id} issue={issue} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}`}>{'<< Back to project'}</Link>
            { issuesView }
        </div>
    )
}

export {
    IssuesPage
}