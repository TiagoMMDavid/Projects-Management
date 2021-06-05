import React, { useEffect, useState } from 'react'
import { deleteProject } from '../../api/projects'
import { Credentials } from '../../utils/userSession'

type DeleteProjectProps = {
    project: Project
    onFinishDelete: (success: boolean, message: string) => void
    credentials: Credentials
}

function DeleteProject({project, onFinishDelete, credentials}: DeleteProjectProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toDelete, setToDelete] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toDelete) {
            setMessage('Deleting project...')

            deleteProject(project.id, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishDelete(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishDelete(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toDelete])

    return (
        <div>
            <button className="danger" onClick={() => setToDelete(true)}>Delete Project</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteProject
}