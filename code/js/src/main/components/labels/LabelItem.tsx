import React from 'react'
import { Link } from 'react-router-dom'

type LabelProps = {
    label: Label
}

function LabelItem({ label }: LabelProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Number: <Link to={`/projects/${label.projectId}/labels/${label.number}`}>{label.number}</Link></p>
                <p>Name: {label.name}</p>
            </li>
        </ul>
    )
}

export {
    LabelItem
}