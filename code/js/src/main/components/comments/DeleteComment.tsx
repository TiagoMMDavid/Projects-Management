import React, { useState } from 'react'
import { deleteComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type DeleteCommentProps = {
    comment: IssueComment
    onFinishDelete: (success: boolean, message: string) => void
    onDelete: () => void
    credentials: Credentials
}

function DeleteComment({comment, onFinishDelete, onDelete, credentials}: DeleteCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)

    function deleteCommentHandler() {
        setMessage('Deleting comment...')
        onDelete()
        deleteComment(comment.projectId, comment.issueNumber, comment.number, credentials)
            .then(res => {
                onFinishDelete(res, 'Failed to delete comment!')
            })
    }

    return (
        <div>
            <button onClick={deleteCommentHandler}>Delete Comment</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteComment
}