import React, { useEffect, useRef, useState } from 'react'
import { createIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type CreateIssueProps = {
    onFinishCreating: (success: boolean, message: string) => void
    credentials: Credentials,
    projectId: number
}

function CreateIssue({onFinishCreating, credentials, projectId}: CreateIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toCreate, setToCreate] = useState(false)

    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toCreate) {
            const nameInput = name.current.value
            const descriptionInput = description.current.value
            if (nameInput.length == 0) {
                setToCreate(false)
                return setMessage('Name can\'t be empty!')
            }
            if (descriptionInput.length == 0) {
                setToCreate(false)
                return setMessage('Description can\'t be empty!')
            }

            name.current.value = ''
            description.current.value = ''

            setMessage('Creating issue...')
            createIssue(projectId, nameInput, descriptionInput, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishCreating(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishCreating(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toCreate])

    return (
        <div>
            <h2>Create Issue</h2>
            <input type="text" maxLength={64} placeholder="Issue Name" ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder="Issue Description" ref={description} onChange={() => setMessage(null)} />
            <button onClick={() => setToCreate(true)}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateIssue
}