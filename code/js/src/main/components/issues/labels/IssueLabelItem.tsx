import React from 'react'
import { Link } from 'react-router-dom'
import { removeLabelFromIssue } from '../../../api/labels'
import { Credentials } from '../../../utils/userSession'

type IssueLabelProps = {
    issue: Issue
    label: Label
    onRemove: () => void
    onFinishRemove: (success: boolean, message: string) => void
    credentials: Credentials
}

function IssueLabelItem({ label, issue, onRemove, onFinishRemove, credentials }: IssueLabelProps): JSX.Element {
    function removeLabel() {
        onRemove()

        removeLabelFromIssue(issue.projectId, issue.number, label.number, credentials)
            .then(() => onFinishRemove(true, null))
            .catch(err => onFinishRemove(false, err.message))
    }

    return (
        <li>
            <p>Name: <Link to={`/projects/${issue.projectId}/labels/${label.number}`}>{label.name}</Link></p>
            <button onClick={removeLabel}>Remove label</button>
        </li>
    )
}

export {
    IssueLabelItem
}