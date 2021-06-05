import React, { useEffect, useRef, useState } from 'react'
import { editProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type EditProjectProps = {
    project: Project
    onFinishEdit: (success: boolean, message: string) => void
    credentials: Credentials
}

function EditProject({project, onFinishEdit, credentials}: EditProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toEdit, setToEdit] = useState(false)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toEdit) {
            const nameInput = name.current.value
            const descriptionInput = description.current.value
            if (nameInput.length == 0 && descriptionInput.length == 0) {
                setToEdit(false)
                return setMessage('You need to change at least one field!')
            }

            const newName = nameInput.length != 0 ? nameInput : null 
            const newDesc = descriptionInput.length != 0 ? descriptionInput : null 

            name.current.value = ''
            description.current.value = ''
            setMessage('Editing project...')
            editProject(project.id, newName, newDesc, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishEdit(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishEdit(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toEdit])

    return (
        <div>
            <h2>Edit Project</h2>
            <input type="text" maxLength={64} placeholder={project.name} ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder={project.description} ref={description} onChange={() => setMessage(null)} />
            <button onClick={() => setToEdit(true)}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditProject
}