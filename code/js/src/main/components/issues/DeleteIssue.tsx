import React, { useEffect, useState } from 'react'
import { deleteIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type DeleteIssueProps = {
    issue: Issue
    onFinishDelete: (success: boolean, message: string) => void
    credentials: Credentials
}

function DeleteIssue({issue, onFinishDelete, credentials}: DeleteIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toDelete, setToDelete] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toDelete) {
            setMessage('Deleting issue...')
            deleteIssue(issue.projectId, issue.number, credentials)
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
            <button className="danger" onClick={() => setToDelete(true)}>Delete Issue</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteIssue
}