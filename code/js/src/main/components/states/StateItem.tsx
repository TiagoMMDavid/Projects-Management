import React from 'react'
import { Link } from 'react-router-dom'

type StateProps = {
    state: IssueState
}

function StateItem({ state }: StateProps): JSX.Element {
    return (
        <li>
            <p>
                Name: <Link to={`/projects/${state.projectId}/states/${state.number}`}>{state.name}</Link> 
                {state.isStartState ? 
                    <span className="startState ml-2">start state</span>
                    : <></>
                }
            </p>
            <p>Author: <Link to={`/users/${state.authorId}`}>{state.author}</Link></p>
        </li>
    )
}

export {
    StateItem
}