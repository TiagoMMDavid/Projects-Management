import React, { useEffect, useRef, useState } from 'react'
import { createState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type CreateStateProps = {
    onFinishCreating: (success: boolean, message: string) => void
    credentials: Credentials,
    projectId: number
}

function CreateState({onFinishCreating, credentials, projectId}: CreateStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toCreate, setToCreate] = useState(false)
    const name = useRef<HTMLInputElement>(null)
    const isStart = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toCreate) {
            const nameInput = name.current.value
            const isStartChecked = isStart.current.checked
            if (nameInput.length == 0) {
                setToCreate(false)
                return setMessage('Name can\'t be empty!')
            }

            name.current.value = ''
            isStart.current.checked = false

            setMessage('Creating state...')
            createState(projectId, nameInput, isStartChecked, credentials)
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
            <h2>Create State</h2>
            <input type="text" maxLength={64} placeholder="State Name" ref={name} onChange={() => setMessage(null)} />
            <input id="isStart" type="checkbox" ref={isStart} />
            <label htmlFor="isStart">Is Start </label>
            <button onClick={() => setToCreate(true)}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateState
}