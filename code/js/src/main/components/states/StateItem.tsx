import React from 'react'
import { Link } from 'react-router-dom'

type StateProps = {
    state: IssueState
}

function StateItem({ state }: StateProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Number: <Link to={`/projects/${state.projectId}/states/${state.number}`}>{state.number}</Link></p>
                <p>Name: {state.name}</p>
                <p>Author: <Link to={`/users/${state.authorId}`}>{state.author}</Link></p>
            </li>
        </ul>
    )
}

export {
    StateItem
}