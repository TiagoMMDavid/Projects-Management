import React, { useEffect, useRef, useState } from 'react'
import { editState } from '../../api/states'
import { Credentials } from '../../utils/userSession'

type EditStateProps = {
    state: IssueState
    onFinishEdit: (success: boolean, message: string) => void
    credentials: Credentials
}

function EditState({state, onFinishEdit, credentials}: EditStateProps): JSX.Element {
    const [message, setMessage] = useState(null)
    const [toEdit, setToEdit] = useState(false)
    const name = useRef<HTMLInputElement>(null)
    const isStart = useRef<HTMLInputElement>(null)

    useEffect(() => {
        let isCancelled = false

        if (toEdit) {
            const nameInput = name.current.value
            const isStartChecked = isStart.current.checked
            if (nameInput.length == 0 && state.isStartState == isStartChecked) {
                setToEdit(false)
                return setMessage('You need to change at least one field!')
            }

            const newName = nameInput.length != 0 ? nameInput : null 
            name.current.value = ''

            setMessage('Editing state...')
            editState(state.projectId, state.number, newName, isStartChecked, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishEdit(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishEdit(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toEdit])

    return (
        <div>
            <h2>Edit State</h2>
            <input type="text" maxLength={64} placeholder={state.name} ref={name} onChange={() => setMessage(null)} />
            <input id="isStart" type="checkbox" ref={isStart} defaultChecked={state.isStartState} onClick={() => setMessage(null)}/>
            <label htmlFor="isStart">Is Start </label>
            <button onClick={() => setToEdit(true)}>Edit</button>
            <p>{message}</p>
        </div>
    )
}

export {
    EditState
}