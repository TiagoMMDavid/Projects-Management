import React from 'react'
import { Credentials } from '../../utils/userSession'

type IssuePageProps = {
    getIssue: (projectId: number, issueNumber: number, credentials: Credentials) => Promise<Issue>
}

function IssuePage({ getIssue }: IssuePageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    IssuePage
}