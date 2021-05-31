import React from 'react'
import { Link } from 'react-router-dom'

type IssueProps = {
    issue: Issue
}

function IssueItem({ issue }: IssueProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Number: <Link to={`/projects/${issue.projectId}/issues/${issue.number}`}>{issue.number}</Link></p>
                <p>Name: {issue.name}</p>
                <p>Description: {issue.description}</p>
                <p>State: {issue.state}</p>
                <p>Author: <Link to={`/users/${issue.authorId}`}>{issue.author}</Link></p>
            </li>
        </ul>
    )
}

export {
    IssueItem
}