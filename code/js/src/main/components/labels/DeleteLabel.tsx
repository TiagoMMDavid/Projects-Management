import React, { useEffect, useState } from 'react'
import { deleteLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type DeleteLabelProps = {
    label: Label
    onFinishDelete: (success: boolean, message: string) => void
    credentials: Credentials
}

function DeleteLabel({label, onFinishDelete, credentials}: DeleteLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toDelete, setToDelete] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toDelete) {
            setMessage('Deleting label...')
            deleteLabel(label.projectId, label.number, credentials)
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
            <button className="danger" onClick={() => setToDelete(true)}>Delete Label</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteLabel
}