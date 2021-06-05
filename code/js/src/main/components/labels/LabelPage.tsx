import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { getSirenAction } from '../../api/apiRoutes'
import { getLabel } from '../../api/labels'
import { UserContext } from '../../utils/userSession'
import { DeleteLabel } from './DeleteLabel'
import { EditLabel } from './EditLabel'

type LabelProps = {
    label: Label
}

type LabelPageParams = {
    projectId: string,
    labelNumber: string
}

function Label({ label }: LabelProps): JSX.Element {
    return (
        <div>
            <p>Id: {label.id}</p>
            <p>Name: {label.name}</p>
            <p>Author: <Link to={`/users/${label.authorId}`}>{label.author}</Link></p>
        </div>
    )
}

type State = {
    state: 'has-label' | 'loading-label' | 'deleted-label' | 'edited-label' | 'message'
    message: string
    label: Label
}
  
type Action =
    { type: 'set-label', label: Label, message: string} |
    { type: 'loading-label', message: string } |
    { type: 'set-deleted-label' } |
    { type: 'set-edited-label' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-label': return { state: 'has-label', label: action.label, message: action.message } as State
        case 'loading-label': return { state: 'loading-label', message: action.message } as State
        case 'set-deleted-label': return { state: 'deleted-label' } as State
        case 'set-edited-label': return { state: 'edited-label' } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function LabelPage(): JSX.Element {
    const { projectId, labelNumber } = useParams<LabelPageParams>()
    const [{ state, label, message }, dispatch] = useReducer(reducer, {state: 'loading-label'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false

        if (state == 'edited-label' || state == 'loading-label') {
            getLabel(Number(projectId), Number(labelNumber), ctx.credentials)
                .then(label => {
                    if (isCancelled) return
                    dispatch({ type: 'set-label', label: label, message: message })
                })
                .catch(err => {
                    if (isCancelled) return
                    dispatch({ type: 'set-message', message: err.message })
                })
        }

        return () => {
            isCancelled = true
        }
    }, [projectId, labelNumber, state])

    if (state == 'deleted-label')
        return (
            <Redirect to={`/projects/${projectId}/labels`} />
        )

    let body: JSX.Element
    switch(state) {
        case 'message':
            body = (
                <h1>{message}</h1>
            )
            break
        case 'edited-label':
        case 'loading-label':
            body = (
                <h1>Loading label...</h1> 
            )
            break
        case 'has-label':
            body = (
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(label.actions, 'edit-label') != null ?
                        <EditLabel
                            label={label}
                            onFinishEdit={(success, message) => {
                                if (success) {
                                    dispatch({ type: 'set-edited-label' })
                                } else {
                                    dispatch({ type: 'loading-label', message: message })
                                }
                            }}
                            credentials={ctx.credentials} 
                        />
                        : <></>
                    }
                    { getSirenAction(label.actions, 'delete-label') != null ?
                        <DeleteLabel
                            label={label}
                            onFinishDelete={(success, message) => {
                                if (success) {
                                    dispatch({ type: 'set-deleted-label' })
                                } else {
                                    dispatch({ type: 'loading-label', message: message })
                                }
                            }}
                            credentials={ctx.credentials}
                        />
                        : <></>
                    }
                    <hr/>
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/labels`}>{'<< View all labels'}</Link>
            {body}
            {label == null ? 
                <></> :
                <div>
                    <h1>Label</h1>
                    <Label label={label}/>
                </div>
            }
        </div>
    )
}

export {
    LabelPage
}