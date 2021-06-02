import React, { useRef, useState } from 'react'
import { createComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type CreateCommentProps = {
    onFinishCreating: (success: boolean, message: string) => void
    onCreating: () => void
    credentials: Credentials
    projectId: number
    issueNumber: number
}

function CreateComment({onFinishCreating, onCreating, credentials, projectId, issueNumber}: CreateCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const content = useRef<HTMLInputElement>(null)

    function createCommentHandler() {

        const contentInput = content.current.value
        if (contentInput.length == 0) {
            return setMessage('Name can\'t be empty!')
        }

        content.current.value = ''

        setMessage('Creating comment...')
        onCreating()
        createComment(projectId, issueNumber, contentInput, credentials)
            .then(res => onFinishCreating(res, res ? null : 'Failed to create comment'))
    }

    return (
        <div>
            <h2>Create Comment</h2>
            <input type="textarea" maxLength={256} placeholder="Comment" ref={content} style={{width: '300px'}}onChange={() => setMessage(null)} />
            <button onClick={createCommentHandler}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateComment
}