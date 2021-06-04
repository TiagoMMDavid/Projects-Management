import React, { useState } from 'react'
import { deleteLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type DeleteLabelProps = {
    label: Label
    onFinishDelete: (success: boolean, message: string) => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteLabel({label, onFinishDelete, onDelete, credentials}: DeleteLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteLabelHandler() {
        setMessage('Deleting label...')
        onDelete()
        deleteLabel(label.projectId, label.number, credentials)
            .then(() => onFinishDelete(true, null))
            .catch(err => onFinishDelete(false, err.message))
    }

    return (
        <div>
            <button className="danger" onClick={deleteLabelHandler}>Delete Label</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteLabel
}