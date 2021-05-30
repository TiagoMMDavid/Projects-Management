import React, { useRef, useState } from 'react'
import { editProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type EditProjectProps = {
    project: Project
    onFinishEdit: () => void
    credentials: Credentials
}

function EditProject({project, onFinishEdit, credentials}: EditProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    function editProjectHandler() {
        const nameInput = name.current.value
        const descriptionInput = description.current.value
        if (nameInput.length == 0 && descriptionInput.length == 0) {
            return setMessage('You need to change at least one field!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        const newDesc = descriptionInput.length != 0 ? descriptionInput : null 

        name.current.value = ''
        description.current.value = ''
        setMessage('Editing project...')
        editProject(project.id, newName, newDesc, credentials)
            .then(res => {
                if (!res) setMessage('Failed to edit project!')

                setMessage(null)
                onFinishEdit()
            })
    }

    return (
        <div>
            <h2>Edit Project</h2>
            <input type="text" maxLength={64} placeholder="Project Name" ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder="Project Description" ref={description} onChange={() => setMessage(null)} />
            <button onClick={editProjectHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditProject
}