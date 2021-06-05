import React, { useEffect, useRef, useState } from 'react'
import { createComment } from '../../api/comments'
import { Credentials } from '../../utils/userSession'

type CreateCommentProps = {
    onFinishCreating: (success: boolean, message: string) => void
    credentials: Credentials
    projectId: number
    issueNumber: number
}

function CreateComment({onFinishCreating, credentials, projectId, issueNumber}: CreateCommentProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toCreate, setToCreate] = useState(false)
    const content = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toCreate) {
            const contentInput = content.current.value
            if (contentInput.length == 0) {
                setToCreate(false)
                return setMessage('Content can\'t be empty!')
            }

            content.current.value = ''
            
            setMessage('Creating comment...')
            createComment(projectId, issueNumber, contentInput, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishCreating(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishCreating(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toCreate])

    return (
        <div>
            <h2>Create Comment</h2>
            <input type="textarea" maxLength={256} placeholder="Comment" ref={content} style={{width: '300px'}} onChange={() => setMessage(null)} />
            <button onClick={() => setToCreate(true)}>Create</button>
            <p>{message}</p>
        </div>
    )
}

export {
    CreateComment
}