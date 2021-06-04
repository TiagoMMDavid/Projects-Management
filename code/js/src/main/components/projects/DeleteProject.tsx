import React, { useState } from 'react'
import { deleteProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type DeleteProjectProps = {
    project: Project
    onFinishDelete: (success: boolean, message: string) => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteProject({project, onFinishDelete, onDelete, credentials}: DeleteProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteProjectHandler() {
        setMessage('Deleting project...')
        onDelete()
        deleteProject(project.id, credentials)
            .then(() => onFinishDelete(true, null))
            .catch(err => onFinishDelete(false, err.message))
    }

    return (
        <div>
            <button className="danger" onClick={deleteProjectHandler}>Delete Project</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteProject
}