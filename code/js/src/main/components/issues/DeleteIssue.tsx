import React, { useState } from 'react'
import { deleteIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type DeleteIssueProps = {
    issue: Issue
    onFinishDelete: () => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteIssue({issue, onFinishDelete, onDelete, credentials}: DeleteIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteIssueHandler() {
        setMessage('Deleting issue...')
        onDelete()
        deleteIssue(issue.projectId, issue.number, credentials)
            .then(res => {
                if (!res) setMessage('Failed to delete issue!')
                else setMessage(null)
                onFinishDelete()
            })
    }

    return (
        <div>
            <button onClick={deleteIssueHandler}>Delete Issue</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteIssue
}