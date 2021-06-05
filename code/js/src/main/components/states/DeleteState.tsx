import React, { useEffect, useState } from 'react'
import { deleteState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type DeleteStateProps = {
    state: IssueState
    onFinishDelete: (success: boolean, message: string) => void
    credentials: Credentials
}

function DeleteState({state, onFinishDelete, credentials}: DeleteStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toDelete, setToDelete] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toDelete) {
            setMessage('Deleting state...')
            deleteState(state.projectId, state.number, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishDelete(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishDelete(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toDelete])

    return (
        <div>
            <button className="danger" onClick={() => setToDelete(true)}>Delete State</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteState
}