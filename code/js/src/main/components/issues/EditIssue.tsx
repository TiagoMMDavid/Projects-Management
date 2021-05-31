import React, { useRef, useState } from 'react'
import { editIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type EditIssueProps = {
    issue: Issue
    onFinishEdit: () => void
    onEdit: () => void
    credentials: Credentials
}

function EditIssue({issue, onFinishEdit, onEdit, credentials}: EditIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    function editIssueHandler() {
        const nameInput = name.current.value
        const descriptionInput = description.current.value
        if (nameInput.length == 0 && descriptionInput.length == 0) {
            return setMessage('You need to change at least one field!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        const newDesc = descriptionInput.length != 0 ? descriptionInput : null 

        name.current.value = ''
        description.current.value = ''

        setMessage('Editing issue...')
        onEdit()
        editIssue(issue.projectId, issue.number, newName, newDesc, null, credentials)
            .then(res => {
                if (!res) setMessage('Failed to edit issue!')
                else setMessage(null)
                onFinishEdit()
            })
    }

    return (
        <div>
            <h2>Edit Issue</h2>
            <input type="text" maxLength={64} placeholder={issue.name} ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder={issue.description} ref={description} onChange={() => setMessage(null)} />
            <select name="TODO" id="TODO">
                <option value="TODO">TODO</option>
            </select>
            <button onClick={editIssueHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditIssue
}