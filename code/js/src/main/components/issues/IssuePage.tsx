import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import { DeleteIssue } from './DeleteIssue'
import { EditIssue } from './EditIssue'

type IssuePageProps = {
    getIssue: (projectId: number, issueNumber: number, credentials: Credentials) => Promise<Issue>
}
type IssueProps = {
    issue: Issue
}

type IssuePageParams = {
    projectId: string,
    issueNumber: string
}

function Issue({ issue }: IssueProps): JSX.Element {
    return (
        <div>
            <p>Number: {issue.number}</p>
            <p>Name: {issue.name}</p>
            <p>Description: {issue.description}</p>
            <p>Current State: {issue.state}</p>
            <p>Creation Date: {new Date(issue.createDate).toLocaleString()}</p>
            {issue.closeDate == null ? <></> : <p>Close Date: {new Date(issue.closeDate).toLocaleString()}</p>}
            <p>Author: <Link to={`/users/${issue.authorId}`}>{issue.author}</Link></p>
            <Link to={`/projects/${issue.projectId}/issues/${issue.number}/comments`}>View comments</Link>
        </div>
    )
}

type State = {
    state: 'has-issue' | 'loading-issue' | 'deleted-issue' | 'edited-issue' | 'message'
    message: string
    issue: Issue
}
  
type Action =
    { type: 'set-issue', issue: Issue} |
    { type: 'loading-issue' } |
    { type: 'set-deleted-issue' } |
    { type: 'set-edited-issue' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-issue': return { state: 'has-issue', issue: action.issue} as State
        case 'loading-issue': return { state: 'loading-issue' } as State
        case 'set-deleted-issue': return { state: 'deleted-issue' } as State
        case 'set-edited-issue': return { state: 'edited-issue' } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function IssuePage({ getIssue }: IssuePageProps): JSX.Element {
    const { projectId, issueNumber } = useParams<IssuePageParams>()
    const [{ state, issue, message }, dispatch] = useReducer(reducer, {state: 'loading-issue'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-issue' || state == 'loading-issue') {
            getIssue(Number(projectId), Number(issueNumber), ctx.credentials)
                .then(issue => {
                    if (issue) dispatch({ type: 'set-issue', issue: issue })
                    else dispatch({ type: 'set-message', message: 'Issue Not Found' })
                })
        }
    }, [projectId, issueNumber, state])

    if (state == 'deleted-issue')
        return (
            <Redirect to={`/projects/${projectId}/issues`} />
        )

    let body: JSX.Element
    switch(state) {
        case 'message':
            body = (
                <h1>{message}</h1>
            )
            break
        case 'edited-issue':
        case 'loading-issue':
            body = (
                <h1>Loading issue...</h1> 
            )
            break
        case 'has-issue':
            body = (
                <div>
                    <EditIssue
                        issue={issue}
                        onFinishEdit={() => dispatch({ type: 'set-edited-issue' })}
                        onEdit={() => dispatch({ type: 'set-message', message: 'Editing Issue...' })}
                        credentials={ctx.credentials} 
                    />
                    <DeleteIssue
                        issue={issue}
                        onFinishDelete={() => dispatch({ type: 'set-deleted-issue' })}
                        onDelete={() => dispatch({ type: 'set-message', message: 'Deleting Issue...' })}
                        credentials={ctx.credentials}
                    />
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/issues`}>View all issues</Link>
            {body}
            {issue == null ? <></> :  <Issue issue={issue}/>}
        </div>
    )
}

export {
    IssuePage
}