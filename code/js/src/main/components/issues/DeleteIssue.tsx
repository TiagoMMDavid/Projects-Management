import React, { useState } from 'react'
import { deleteIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type DeleteIssueProps = {
    issue: Issue
    onFinishDelete: (success: boolean, message: string) => void
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
                onFinishDelete(res, 'Failed to delete issue!')
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