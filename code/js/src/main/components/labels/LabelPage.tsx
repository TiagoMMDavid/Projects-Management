import React, { useContext, useEffect, useReducer } from 'react'
import { Link, Redirect, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'

type LabelProps = {
    label: Label
}

type LabelPageParams = {
    projectId: string,
    labelNumber: string,
}

function Label({ label }: LabelProps): JSX.Element {
    return (
        <div>
            <p>Number: {label.number}</p>
            <p>Name: {label.name}</p>
        </div>
    )
}

type State = {
    state: 'has-label' | 'loading-label' | 'no-label' | 'deleted-label' | 'edited-label'
    label: Label
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-label', label: Label} |
    { type: 'set-no-label' } |
    { type: 'set-deleted-label' } |
    { type: 'set-edited-label' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-label', label: null}
        case 'set-label': return { state: 'has-label', label: action.label}
        case 'set-no-label': return { state: 'no-label', label: null}
        case 'set-deleted-label': return { state: 'deleted-label', label: null}
        case 'set-edited-label': return { state: 'edited-label', label: null}
    }
}

type LabelPageProps = {
    getLabel: (projectId: number, labelNumber: number, credentials: Credentials) => Promise<Label>
}

function LabelPage({ getLabel }: LabelPageProps): JSX.Element {
    const { projectId, labelNumber } = useParams<LabelPageParams>()
    const [{ state, label }, dispatch] = useReducer(reducer, {state: 'loading-label', label: null})
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-label' || state == 'loading-label') {
            getLabel(Number(projectId), Number(labelNumber), ctx.credentials)
                .then(label => {
                    if (label) dispatch({ type: 'set-label', label: label })
                    else dispatch({ type: 'set-no-label' })
                })
        }
    }, [labelNumber, state])

    if (state == 'deleted-label')
        return (
            <Redirect to={`/projects/${projectId}/labels`} />
        )

    let toReturn: JSX.Element
    switch(state) {
        case 'edited-label':
        case 'loading-label': 
            toReturn = (
                <h1> Loading label... </h1> 
            )
            break
        case 'no-label':
            toReturn = (
                <h1> Label not found </h1> 
            )
            break
        case 'has-label':
            toReturn = (
                <div>
                    {/* TODO: Edit and Delete label*/}
                    <Label label={label}/>
                </div>
            )
            break
    }
    return (
        <div>
            <Link to={`/projects/${projectId}/labels`}>View all labels</Link>
            {toReturn}
        </div>
    )
}

export {
    LabelPage
}