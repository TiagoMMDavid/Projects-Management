import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { getIssue } from '../../api/issues'
import { getNextStates } from '../../api/states'
import { UserContext } from '../../utils/userSession'
import { DeleteIssue } from './DeleteIssue'
import { EditIssue } from './EditIssue'

type IssueProps = {
    issue: Issue
}
type IssueNextStates = {
    issue: Issue,
    nextStates: IssueStates
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
            <p>Current State: <Link to={`/projects/${issue.projectId}/states/${issue.stateNumber}`}>{issue.state}</Link></p>
            <p>Creation Date: {new Date(issue.createDate).toLocaleString()}</p>
            {issue.closeDate == null ? <></> : <p>Close Date: {new Date(issue.closeDate).toLocaleString()}</p>}
            <p>Author: <Link to={`/users/${issue.authorId}`}>{issue.author}</Link></p>
            <p><Link to={`/projects/${issue.projectId}/issues/${issue.number}/comments`}>View comments</Link></p>
            <p><Link to={`/projects/${issue.projectId}/issues/${issue.number}/labels`}>View labels</Link></p>
        </div>
    )
}

type State = {
    state: 'has-issue' | 'loading-issue' | 'deleted-issue' | 'edited-issue' | 'message'
    message: string
    issueStates: IssueNextStates
}
  
type Action =
    { type: 'set-issue', issueStates: IssueNextStates, message: string } |
    { type: 'loading-issue', message: string } |
    { type: 'set-deleted-issue' } |
    { type: 'set-edited-issue' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-issue': return { state: 'has-issue', issueStates: action.issueStates, message: action.message } as State
        case 'loading-issue': return { state: 'loading-issue', message: action.message } as State
        case 'set-deleted-issue': return { state: 'deleted-issue' } as State
        case 'set-edited-issue': return { state: 'edited-issue' } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function IssuePage(): JSX.Element {
    const { projectId, issueNumber } = useParams<IssuePageParams>()
    const [{ state, issueStates, message }, dispatch] = useReducer(reducer, {state: 'loading-issue'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-issue' || state == 'loading-issue') {
            getIssue(Number(projectId), Number(issueNumber), ctx.credentials)
                .then(async issue => {
                    return {
                        issue: issue,
                        nextStates: await getNextStates(issue.projectId, issue.stateNumber, ctx.credentials)
                    } as IssueNextStates
                })
                .then(issueNextStates => dispatch({type: 'set-issue', issueStates: issueNextStates, message: message}))
                .catch(err => dispatch({ type: 'set-message', message: err.message }))
        }
    }, [state])

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
                    <h4>{message}</h4>
                    <EditIssue
                        issue={issueStates.issue}
                        nextStates={issueStates.nextStates}
                        onFinishEdit={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-edited-issue' })
                            } else {
                                dispatch({ type: 'loading-issue', message: message })
                            }
                        }}
                        credentials={ctx.credentials} 
                    />
                    <DeleteIssue
                        issue={issueStates.issue}
                        onFinishDelete={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-deleted-issue' })
                            } else {
                                dispatch({ type: 'loading-issue', message: message })
                            }
                        }}
                        credentials={ctx.credentials}
                    />
                    <hr/>
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/issues`}>{'<< View all issues'}</Link>
            {body}
            {issueStates?.issue == null ? 
                <></> :
                <div>
                    <h1>Issue</h1>
                    <Issue issue={issueStates.issue}/>
                </div>
            }
        </div>
    )
}

export {
    IssuePage
}