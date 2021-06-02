import React from 'react'
import { Link } from 'react-router-dom'

type CommentProps = {
    comment: IssueComment
}

function CommentItem({ comment }: CommentProps): JSX.Element {
    const date = new Date(comment.createDate).toLocaleString()
    return (
        <li>
            <p>Content: {comment.content}</p>
            <p>Date: <Link to={`/projects/${comment.projectId}/issues/${comment.issueNumber}/comments/${comment.number}`}>{date}</Link></p>
            <p>Author: <Link to={`/users/${comment.authorId}`}>{comment.author}</Link></p>
        </li>
    )
}

export {
    CommentItem
}