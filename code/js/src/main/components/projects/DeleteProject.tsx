import React, { useState } from 'react'
import { deleteProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type DeleteProjectProps = {
    project: Project
    onFinishDelete: () => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteProject({project, onFinishDelete, onDelete, credentials}: DeleteProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteProjectHandler() {
        setMessage('Deleting project...')
        onDelete()
        deleteProject(project.id, credentials)
            .then(res => {
                if (!res) setMessage('Failed to delete project!')
                else setMessage(null)
                onFinishDelete()
            })
    }

    return (
        <div>
            <button onClick={deleteProjectHandler}>Delete Project</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteProject
}