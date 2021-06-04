import React, { useRef, useState } from 'react'
import { editLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type EditLabelProps = {
    label: Label
    onFinishEdit: (success: boolean, message: string) => void
    onEdit: () => void
    credentials: Credentials
}

function EditLabel({label, onFinishEdit, onEdit, credentials}: EditLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)

    function editLabelHandler() {
        const nameInput = name.current.value
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        name.current.value = ''

        setMessage('Editing label...')
        onEdit()
        editLabel(label.projectId, label.number, newName, credentials)
            .then(() => onFinishEdit(true, null))
            .catch(err => onFinishEdit(false, err.message))
    }

    return (
        <div>
            <h2>Edit Label</h2>
            <input type="text" maxLength={64} placeholder={label.name} ref={name} onChange={() => setMessage(null)} />
            <button onClick={editLabelHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditLabel
}