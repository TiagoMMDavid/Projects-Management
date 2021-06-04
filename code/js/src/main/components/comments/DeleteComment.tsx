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
            .then(() => onFinishDelete(true, null))
            .catch(err => onFinishDelete(false, err.message))
    }

    return (
        <div>
            <button className="danger" onClick={deleteCommentHandler}>Delete Comment</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteComment
}