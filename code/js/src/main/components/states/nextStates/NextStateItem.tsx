import React from 'react'
import { Link } from 'react-router-dom'
import { getSirenAction } from '../../../api/apiRoutes'
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
            .then(() => onFinishRemove(true, null))
            .catch(err => onFinishRemove(false, err.message))
    }

    return (
        <li>
            <p>Name: <Link to={`/projects/${state.projectId}/states/${nextState.number}`}>{nextState.name}</Link></p>
            { getSirenAction(nextState.actions, 'delete-next-state') == null ? <></> : <button onClick={removeState}>Remove state</button> }
        </li>
    )
}

export {
    NextStateItem
}