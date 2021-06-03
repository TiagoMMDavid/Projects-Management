import React, { useRef, useState } from 'react'
import { editIssue } from '../../api/issues'
import { Credentials } from '../../utils/userSession'

type EditIssueProps = {
    issue: Issue
    nextStates: IssueStates
    onFinishEdit: (success: boolean, message: string) => void
    onEdit: () => void
    credentials: Credentials
}

function EditIssue({issue, nextStates, onFinishEdit, onEdit, credentials}: EditIssueProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [nextState, setNextState] = useState(issue.state)
    const name = useRef<HTMLInputElement>(null)
    const description = useRef<HTMLInputElement>(null)

    function editIssueHandler() {
        const nameInput = name.current.value
        const descriptionInput = description.current.value
        if (nameInput.length == 0 && descriptionInput.length == 0 && nextState == issue.state) {
            return setMessage('You need to change at least one field!')
        }

        const newName = nameInput.length != 0 ? nameInput : null 
        const newDesc = descriptionInput.length != 0 ? descriptionInput : null 

        name.current.value = ''
        description.current.value = ''

        setMessage('Editing issue...')
        onEdit()
        editIssue(issue.projectId, issue.number, newName, newDesc, nextState, credentials)
            .then(res => {
                onFinishEdit(res, 'Failed to edit issue!')
            })
    }

    function changeStateHandle(e: React.ChangeEvent<HTMLSelectElement>) {
        setNextState(e.target.value)
    }

    return (
        <div>
            <h2>Edit Issue</h2>
            <input type="text" maxLength={64} placeholder={issue.name} ref={name} onChange={() => setMessage(null)} />
            <input type="text" maxLength={256} placeholder={issue.description} ref={description} onChange={() => setMessage(null)} />
            <select value={nextState} onChange={changeStateHandle}>
                <option value={issue.state} key={issue.stateNumber}>{issue.state}</option>
                {nextStates.states.map(
                    state => 
                        <option value={state.name} key={state.number}>{state.name}</option>
                )}
            </select>
            <button onClick={editIssueHandler}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditIssue
}