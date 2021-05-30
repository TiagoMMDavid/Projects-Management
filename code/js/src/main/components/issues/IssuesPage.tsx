import React from 'react'
import { Credentials } from '../../utils/userSession'

type IssuesGetter = (projectId: number, page: number, credentials: Credentials) => Promise<Issues>

type IssuesPageProps = {
    getIssues: IssuesGetter
}

function IssuesPage({ getIssues }: IssuesPageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    IssuesPage
}