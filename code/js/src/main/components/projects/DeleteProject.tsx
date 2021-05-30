import React, { useState } from 'react'
import { deleteProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type DeleteProjectProps = {
    project: Project
    onFinishDelete: () => void
    credentials: Credentials
}

function DeleteProject({project, onFinishDelete, credentials}: DeleteProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteProjectHandler() {
        setMessage('Deleting project...')
        deleteProject(project.id, credentials)
            .then(res => {
                if (!res) setMessage('Failed to delete project!')

                setMessage(null)
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