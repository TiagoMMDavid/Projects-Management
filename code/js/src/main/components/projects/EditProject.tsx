import React, { useRef, useState } from 'react'
import { editProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type EditProjectProps = {
    project: Project
    onFinishEdit: (success: boolean, message: string) => void
    onEdit: () => void
    credentials: Credentials
}

function EditProject({project, onFinishEdit, onEdit, credentials}: EditProjectProps): JSX.Element {
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
        onEdit()
        editProject(project.id, newName, newDesc, credentials)
            .then(res => {
                onFinishEdit(res, 'Failed to edit project!')
            })
    }

    return (
        <div>
            <h2>Edit Project</h2>
            <input type="text" maxLength={64} placeholder={project.name} ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder={project.description} ref={description} onChange={() => setMessage(null)} />
            <button onClick={editProjectHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditProject
}