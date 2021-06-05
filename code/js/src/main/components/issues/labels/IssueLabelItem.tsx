import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { removeLabelFromIssue } from '../../../api/labels'
import { Credentials } from '../../../utils/userSession'

type IssueLabelProps = {
    issue: Issue
    label: Label
    onFinishRemove: (success: boolean, message: string) => void
    credentials: Credentials
}

function IssueLabelItem({ label, issue, onFinishRemove, credentials }: IssueLabelProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toRemove, setToRemove] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toRemove) {
            setMessage('Removing label...')
            removeLabelFromIssue(issue.projectId, issue.number, label.number, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishRemove(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishRemove(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toRemove])

    return (
        <li>
            <p>Name: <Link to={`/projects/${issue.projectId}/labels/${label.number}`}>{label.name}</Link></p>
            <button className="danger" onClick={() => setToRemove(true)}>Remove label</button>
            <p>{message}</p>
        </li>
    )
}

export {
    IssueLabelItem
}