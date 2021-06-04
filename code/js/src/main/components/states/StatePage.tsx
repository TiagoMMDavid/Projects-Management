import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { getSirenAction } from '../../api/apiRoutes'
import { getState } from '../../api/states'
import { UserContext } from '../../utils/userSession'
import { DeleteState } from './DeleteState'
import { EditState } from './EditState'

type StateProps = {
    issueState: IssueState
}

type StatePageParams = {
    projectId: string,
    stateNumber: string
}

function State({ issueState }: StateProps): JSX.Element {
    return (
        <div>
            <p>Number: {issueState.number}</p>
            <p>Name: {issueState.name}</p>
            <p>Start State: {issueState.isStartState ? 'Yes' : 'No' }</p>
            <p>Author: <Link to={`/users/${issueState.authorId}`}>{issueState.author}</Link></p>
        </div>
    )
}

type State = {
    state: 'has-state' | 'loading-state' | 'deleted-state' | 'edited-state' | 'state-fail' | 'message'
    message: string
    issueState: IssueState
}
  
type Action =
    { type: 'set-state', issueState: IssueState, message: string} |
    { type: 'loading-state', message: string } |
    { type: 'set-deleted-state' } |
    { type: 'set-edited-state' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-state': return { state: 'has-state', issueState: action.issueState, message: action.message} as State
        case 'loading-state': return { state: 'loading-state', message: action.message } as State
        case 'set-deleted-state': return { state: 'deleted-state' } as State
        case 'set-edited-state': return { state: 'edited-state' } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function StatePage(): JSX.Element {
    const { projectId, stateNumber } = useParams<StatePageParams>()
    const [{ state, issueState, message }, dispatch] = useReducer(reducer, {state: 'loading-state'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-state' || state == 'loading-state') {
            getState(Number(projectId), Number(stateNumber), ctx.credentials)
                .then(issueState => dispatch({ type: 'set-state', issueState: issueState, message: message }))
                .catch(err => dispatch({ type: 'set-message', message: err.message }))
        }
    }, [projectId, stateNumber, state])

    if (state == 'deleted-state')
        return (
            <Redirect to={`/projects/${projectId}/states`} />
        )

    let body: JSX.Element
    switch(state) {
        case 'message':
            body = (
                <h1>{message}</h1>
            )
            break
        case 'edited-state':
        case 'loading-state':
            body = (
                <h1>Loading state...</h1> 
            )
            break
        case 'has-state':
            body = (
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(issueState.actions, 'edit-state') != null ?
                        <EditState
                            state={issueState}
                            onFinishEdit={(success, message) => {
                                if (success) {
                                    dispatch({ type: 'set-edited-state' })
                                } else {
                                    dispatch({ type: 'loading-state', message: message })
                                }
                            }}
                            onEdit={() => dispatch({ type: 'set-message', message: 'Editing State...' })}
                            credentials={ctx.credentials} 
                        />
                        : <></>
                    }
                    { getSirenAction(issueState.actions, 'delete-state') != null ?
                        <DeleteState
                            state={issueState}
                            onFinishDelete={(success, message) => {
                                if (success) {
                                    dispatch({ type: 'set-deleted-state' })
                                } else {
                                    dispatch({ type: 'loading-state', message: message })
                                }
                            }}
                            onDelete={() => dispatch({ type: 'set-message', message: 'Deleting State...' })}
                            credentials={ctx.credentials}
                        />
                        : <></>
                    }
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/states`}>{'<< View all states'}</Link>
            {body}
            {issueState == null ? <></> :  
                <div>
                    <State issueState={issueState}/>
                    <Link to={`/projects/${projectId}/states/${issueState.number}/nextStates`}>View state transitions</Link>
                </div>
                
            }
            
        </div>
    )
}

export {
    StatePage
}