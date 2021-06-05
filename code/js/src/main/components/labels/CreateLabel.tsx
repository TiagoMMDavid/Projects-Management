import React, { useEffect, useRef, useState } from 'react'
import { createLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type CreateLabelProps = {
    onFinishCreating: (success: boolean, message: string) => void
    credentials: Credentials,
    projectId: number
}

function CreateLabel({onFinishCreating, credentials, projectId}: CreateLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toCreate, setToCreate] = useState(false)
    const name = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toCreate) {
            const nameInput = name.current.value
            if (nameInput.length == 0) {
                setToCreate(false)
                return setMessage('Name can\'t be empty!')
            }

            name.current.value = ''
            setMessage('Creating label...')
            createLabel(projectId, nameInput, credentials)
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
            <h2>Create Label</h2>
            <input type="text" maxLength={64} placeholder="Label Name" ref={name} onChange={() => setMessage(null)} />
            <button onClick={() => setToCreate(true)}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateLabel
}