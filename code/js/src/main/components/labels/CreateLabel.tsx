import React, { useRef, useState } from 'react'
import { createLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type CreateLabelProps = {
    onFinishCreating: () => void
    onCreating: () => void
    credentials: Credentials,
    projectId: number
}

function CreateLabel({onFinishCreating, onCreating, credentials, projectId}: CreateLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)

    function createLabelHandler() {

        const nameInput = name.current.value
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        name.current.value = ''

        setMessage('Creating label...')
        onCreating()
        createLabel(projectId, nameInput, credentials)
            .then(() => {
                setMessage(null)
                onFinishCreating()
            })
            .catch(err => {
                setMessage(err.message)
                onFinishCreating()
            })
    }

    return (
        <div>
            <h2>Create Label</h2>
            <input type="text" maxLength={64} placeholder="Label Name" ref={name} onChange={() => setMessage(null)} />
            <button onClick={createLabelHandler}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateLabel
}