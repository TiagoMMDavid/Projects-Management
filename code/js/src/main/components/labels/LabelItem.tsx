import React from 'react'
import { Link } from 'react-router-dom'

type LabelProps = {
    label: Label
}

function LabelItem({ label }: LabelProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Name: <Link to={`/projects/${label.projectId}/labels/${label.number}`}>{label.name}</Link></p>
                <p>Author: <Link to={`/users/${label.authorId}`}>{label.author}</Link></p>
            </li>
        </ul>
    )
}

export {
    LabelItem
}