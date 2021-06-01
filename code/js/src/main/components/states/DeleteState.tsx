import React, { useState } from 'react'
import { deleteState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type DeleteStateProps = {
    state: IssueState
    onFinishDelete: (success: boolean, message: string) => void
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
                onFinishDelete(res, 'Failed to delete state!')
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