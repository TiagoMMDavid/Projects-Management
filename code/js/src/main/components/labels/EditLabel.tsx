import React, { useEffect, useRef, useState } from 'react'
import { editLabel } from '../../api/labels'
import { Credentials } from '../../utils/userSession'

type EditLabelProps = {
    label: Label
    onFinishEdit: (success: boolean, message: string) => void
    credentials: Credentials
}

function EditLabel({label, onFinishEdit, credentials}: EditLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toEdit, setToEdit] = useState(false)
    const name = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toEdit) {
            const nameInput = name.current.value
            if (nameInput.length == 0) {
                setToEdit(false)
                return setMessage('Name can\'t be empty!')
            }
    
            const newName = nameInput.length != 0 ? nameInput : null 
            name.current.value = ''
    
            setMessage('Editing label...')
            editLabel(label.projectId, label.number, newName, credentials)
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
            <h2>Edit Label</h2>
            <input type="text" maxLength={64} placeholder={label.name} ref={name} onChange={() => setMessage(null)} />
            <button onClick={() => setToEdit(true)}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditLabel
}