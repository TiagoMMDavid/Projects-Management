import React, { useRef, useState } from 'react'
import { createState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type CreateStateProps = {
    onFinishCreating: () => void
    onCreating: () => void
    credentials: Credentials,
    projectId: number
}

function CreateState({onFinishCreating, onCreating, credentials, projectId}: CreateStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)
    const isStart = useRef<HTMLInputElement>(null)

    function createStateHandler() {

        const nameInput = name.current.value
        const isStartChecked = isStart.current.checked
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        name.current.value = ''
        isStart.current.checked = false

        setMessage('Creating state...')
        onCreating()
        createState(projectId, nameInput, isStartChecked, credentials)
            .then(res => {
                if (!res) setMessage('Failed to create state!')
                else setMessage(null)
                onFinishCreating()
            })
    }

    return (
        <div>
            <h2>Create State</h2>
            <input type="text" maxLength={64} placeholder="State Name" ref={name} onChange={() => setMessage(null)} />
            <input type="checkbox" ref={isStart} id="isStart"/>
            <label htmlFor="isStart">Is Start </label>
            <button onClick={createStateHandler}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateState
}