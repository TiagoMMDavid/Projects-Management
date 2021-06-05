import React, { useEffect, useRef, useState } from 'react'
import { createProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type CreateProjectProps = {
    onFinishCreating: (success: boolean, message: string) => void
    credentials: Credentials
}

function CreateProject({onFinishCreating, credentials}: CreateProjectProps): JSX.Element {
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

            setMessage('Creating project...')
            createProject(nameInput, descriptionInput, credentials)
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
            <h2>Create Project</h2>
            <input type="text" maxLength={64} placeholder="Project Name" ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder="Project Description" ref={description} onChange={() => setMessage(null)} />
            <button onClick={() => setToCreate(true)}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateProject
}