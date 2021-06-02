import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { IssueItem } from './IssueItem'
import { CreateIssue } from './CreateIssue'

type IssuesGetter = (projectId: number, page: number, credentials: Credentials) => Promise<Issues>

type IssuesPageProps = {
    getIssues: IssuesGetter
}

type IssuesPageParams = {
    projectId: string
}

type State = {
    state: 'has-issues' | 'loading-issues' | 'page-reset' | 'hide' | 'message'
    issues: Issues
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-issues', issues: Issues } |
    { type: 'reset-page' } |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-issues', issues: null } as State
        case 'set-issues': return { state: 'has-issues', issues: action.issues } as State
        case 'reset-page': return { state: 'page-reset', issues: null } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function IssuesPage({ getIssues }: IssuesPageProps): JSX.Element {
    const { projectId } = useParams<IssuesPageParams>()

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, issues, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getIssues(Number(projectId), page, ctx.credentials)
            .then(issues => {
                if (!issues) dispatch({ type: 'set-message', message: 'Failed to get issues' } as Action)
                else dispatch({ type: 'set-issues', issues: issues})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let issuesView: JSX.Element
    switch(state) {
        case 'message':
            issuesView = <h1>{message}</h1>
            break
        case 'hide':
            break
        case 'page-reset':
        case 'loading-issues':
            issuesView = <h1>Loading issues...</h1>
            break
        case 'has-issues':
            issuesView = 
                <div>
                    <Paginated onChangePage={getPage} isLastPage={issues.isLastPage} page={issues.page}>
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
            <Link to={`/projects/${projectId}`}>Back to project</Link>
            <CreateIssue
                onFinishCreating={() => dispatch({type: 'reset-page'})} 
                onCreating={() => dispatch({type: 'hide'})}
                credentials={ctx.credentials} 
                projectId={Number(projectId)}
            />
            { issuesView }
        </div>
    )
}

export {
    IssuesPage
}