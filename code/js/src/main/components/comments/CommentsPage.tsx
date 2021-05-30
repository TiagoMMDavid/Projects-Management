import React from 'react'
import { Credentials } from '../../utils/userSession'

type CommentsGetter = (projectId: number, issueNumber: number, page: number, credentials: Credentials) => Promise<IssueComments>

type CommentsPageProps = {
    getComments: CommentsGetter
}

function CommentsPage({ getComments }: CommentsPageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    CommentsPage
}