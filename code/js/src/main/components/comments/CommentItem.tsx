import React from 'react'
import { Link } from 'react-router-dom'

type CommentProps = {
    comment: IssueComment
}

function CommentItem({ comment }: CommentProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Number: <Link to={`/projects/${comment.projectId}/issues/${comment.issueNumber}/comments/${comment.number}`}>{comment.number}</Link></p>
                <p>Content: {comment.content}</p>
                <p>Author: <Link to={`/users/${comment.authorId}`}>{comment.author}</Link></p>
            </li>
        </ul>
    )
}

export {
    CommentItem
}