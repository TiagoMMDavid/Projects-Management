import React, { useEffect, useState } from 'react'
import { deleteComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type DeleteCommentProps = {
    comment: IssueComment
    onFinishDelete: (success: boolean, message: string) => void
    credentials: Credentials
}

function DeleteComment({comment, onFinishDelete, credentials}: DeleteCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toDelete, setToDelete] = useState(false)

    useEffect(() => {
        let isCancelled = false

        if (toDelete) {
            setMessage('Deleting comment...')
            deleteComment(comment.projectId, comment.issueNumber, comment.number, credentials)
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
            <button className="danger" onClick={() => setToDelete(true)}>Delete Comment</button>
            <p>{message}</p>
        </div>
    )
}

export {
    DeleteComment
}