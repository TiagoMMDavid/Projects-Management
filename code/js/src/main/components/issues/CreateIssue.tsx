import React, { useRef, useState } from 'react'
import { createIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type CreateIssueProps = {
    onFinishCreating: () => void
    onCreating: () => void
    credentials: Credentials,
    projectId: number
}

function CreateIssue({onFinishCreating, onCreating, credentials, projectId}: CreateIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    function createIssueHandler() {
        const nameInput = name.current.value
        const descriptionInput = description.current.value
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }
        if (descriptionInput.length == 0) {
            return setMessage('Description can\'t be empty!')
        }

        name.current.value = ''
        description.current.value = ''

        setMessage('Creating issue...')
        onCreating()
        createIssue(projectId, nameInput, descriptionInput, credentials)
            .then(res => {
                if (!res) setMessage('Failed to create issue!')
                else setMessage(null)
                onFinishCreating()
            })
    }

    return (
        <div>
            <h2>Create Issue</h2>
            <input type="text" maxLength={64} placeholder="Issue Name" ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder="Issue Description" ref={description} onChange={() => setMessage(null)} />
            <button onClick={createIssueHandler}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateIssue
}