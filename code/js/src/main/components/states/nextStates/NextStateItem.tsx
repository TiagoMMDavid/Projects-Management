import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getSirenAction } from '../../../api/apiRoutes'
import { deleteNextState } from '../../../api/states'
import { Credentials } from '../../../utils/userSession'

type IssueStateProps = {
    state: IssueState
    nextState: IssueState
    onFinishRemove: (success: boolean, message: string) => void
    credentials: Credentials
}

function NextStateItem({ state, nextState, onFinishRemove, credentials }: IssueStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toRemove, setToRemove] = useState(false)
    
    useEffect(() => {
        let isCancelled = false

        if (toRemove) {
            setMessage('Removing state...')
            deleteNextState(state.projectId, state.number, nextState.number, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishRemove(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishRemove(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toRemove])

    return (
        <li>
            <p>Name: <Link to={`/projects/${state.projectId}/states/${nextState.number}`}>{nextState.name}</Link></p>
            { getSirenAction(nextState.actions, 'delete-next-state') == null ? <></> : <button className="danger" onClick={() => setToRemove(true)}>Remove state</button> }
            <p>{message}</p>
        </li>
    )
}

export {
    NextStateItem
}