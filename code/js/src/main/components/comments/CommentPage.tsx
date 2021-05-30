import React from 'react'
import { Credentials } from '../../utils/userSession'

type CommentPageProps = {
    getComment: (projectId: number, issueNumber: number, commentNumber: number, credentials: Credentials) => Promise<IssueComment>
}

function CommentPage({ getComment }: CommentPageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    CommentPage
}