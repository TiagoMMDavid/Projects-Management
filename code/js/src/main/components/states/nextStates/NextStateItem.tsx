import React from 'react'
import { Link } from 'react-router-dom'
import { deleteNextState } from '../../../api/states'
import { Credentials } from '../../../utils/userSession'

type IssueStateProps = {
    state: IssueState
    nextState: IssueState,
    onRemove: () => void
    onFinishRemove: (success: boolean, message: string) => void
    credentials: Credentials
}

function NextStateItem({ state, nextState, onRemove, onFinishRemove, credentials }: IssueStateProps): JSX.Element {
    function removeState() {
        onRemove()

        deleteNextState(state.projectId, state.number, nextState.number, credentials)
            .then(success => {
                onFinishRemove(success, success ? '' : 'Error removing state')
            })
    }

    return (
        <li>
            <p>Name: <Link to={`/projects/${state.projectId}/states/${nextState.number}`}>{nextState.name}</Link></p>
            <button onClick={removeState}>Remove state</button>
        </li>
    )
}

export {
    NextStateItem
}