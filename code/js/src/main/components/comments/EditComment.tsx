import React, { useRef, useState } from 'react'
import { editComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type EditCommentProps = {
    comment: IssueComment
    onFinishEdit: (success: boolean, message: string) => void
    onEdit: () => void
    credentials: Credentials
}

function EditComment({comment, onFinishEdit, onEdit, credentials}: EditCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const name = useRef<HTMLInputElement>(null)

    function editCommentHandler() {
        const nameInput = name.current.value
        if (nameInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        name.current.value = ''

        setMessage('Editing comment...')
        onEdit()
        editComment(comment.projectId, comment.issueNumber, comment.number, newName, credentials)
            .then(() => onFinishEdit(true, null))
            .catch(err => onFinishEdit(false, err.message))
    }

    return (
        <div>
            <h2>Edit Comment</h2>
            <input type="text" maxLength={256} placeholder={comment.content} ref={name} onChange={() => setMessage(null)} />
            <button onClick={editCommentHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditComment
}