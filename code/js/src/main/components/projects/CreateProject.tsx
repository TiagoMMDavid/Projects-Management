import React, { useRef, useState } from 'react'
import { createProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type CreateProjectProps = {
    onFinishCreating: () => void
    credentials: Credentials
}

function CreateProject({onFinishCreating, credentials}: CreateProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    function createProjectHandler() {
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

        setMessage('Creating project...')
        createProject(nameInput, descriptionInput, credentials)
            .then(res => {
                if (!res) setMessage('Failed to create project!')

                setMessage(null)
                onFinishCreating()
            })
    }

    return (
        <div>
            <h2>Create Project</h2>
            <input type="text" maxLength={64} placeholder="Project Name" ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder="Project Description" ref={description} onChange={() => setMessage(null)} />
            <button onClick={createProjectHandler}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateProject
}