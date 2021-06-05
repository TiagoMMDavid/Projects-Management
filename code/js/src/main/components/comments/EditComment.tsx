import React, { useEffect, useRef, useState } from 'react'
import { editComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type EditCommentProps = {
    comment: IssueComment
    onFinishEdit: (success: boolean, message: string) => void
    credentials: Credentials
}

function EditComment({comment, onFinishEdit, credentials}: EditCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toEdit, setToEdit] = useState(false)
    const content = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toEdit) {
            const contentInput = content.current.value
            if (contentInput.length == 0) {
                setToEdit(false)
                return setMessage('Content can\'t be empty!')
            }

            const newContent = contentInput.length != 0 ? contentInput : null 
            content.current.value = ''

            setMessage('Editing comment...')
            editComment(comment.projectId, comment.issueNumber, comment.number, newContent, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishEdit(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishEdit(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toEdit])

    return (
        <div>
            <h2>Edit Comment</h2>
            <input type="text" maxLength={256} placeholder={comment.content} ref={content} onChange={() => setMessage(null)} />
            <button onClick={() => setToEdit(true)}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditComment
}