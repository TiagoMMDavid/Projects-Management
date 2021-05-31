import React, { useState } from 'react'
import { deleteState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type DeleteStateProps = {
    state: IssueState
    onFinishDelete: () => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteState({state, onFinishDelete, onDelete, credentials}: DeleteStateProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteStateHandler() {
        setMessage('Deleting state...')
        onDelete()
        deleteState(state.projectId, state.number, credentials)
            .then(res => {
                if (!res) setMessage('Failed to delete state!')
                else setMessage(null)
                onFinishDelete()
            })
    }

    return (
        <div>
            <button onClick={deleteStateHandler}>Delete State</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteState
}