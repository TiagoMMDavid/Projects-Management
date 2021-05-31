import React, { useRef, useState } from 'react'
import { editState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type EditStateProps = {
    state: IssueState
    onFinishEdit: () => void
    onEdit: () => void
    credentials: Credentials
}

function EditState({state, onFinishEdit, onEdit, credentials}: EditStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)

    function editStateHandler() {
        const nameInput = name.current.value
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        name.current.value = ''

        setMessage('Editing state...')
        onEdit()
        editState(state.projectId, state.number, newName, null, credentials)
            .then(res => {
                if (!res) setMessage('Failed to edit state!')
                else setMessage(null)
                onFinishEdit()
            })
    }

    return (
        <div>
            <h2>Edit State</h2>
            <input type="text" maxLength={64} placeholder={state.name} ref={name} onChange={() => setMessage(null)} />
            <button onClick={editStateHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditState
}