import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { addNextState, searchStates } from '../../../api/states'
import { Credentials } from '../../../utils/userSession'

type SearchStatesProps = {
    state: IssueState
    onAdd: () => void
    onFinishAdd: (success: boolean, message: string) => void
    credentials: Credentials
}

function SearchIssueStates({ state, onAdd, onFinishAdd, credentials }: SearchStatesProps): JSX.Element {
    const [searchResults, setSearchResults] = useState<IssueStates>(null)
    const [search, setSearch] = useState<string>(null)
    const [message, setMessage] = useState(null)

    useEffect(() => {
        if (search && search.length > 0) {
            setMessage('Searching...')
            let cancelled = false
            const tid = setTimeout(() => {
                searchStates(state.projectId, search, state.id, credentials)
                    .then(states => {
                        if (cancelled) return

                        if (states.states.length == 0) return setMessage('No states found')

                        setMessage(null)
                        setSearchResults(states)
                    })
                    .catch(err => setMessage(err.message))
            }, 500)

            return () => {
                clearTimeout(tid)
                cancelled = true
            }
        }
    }, [search])

    function handleSearch(e: React.ChangeEvent<HTMLInputElement>) {
        const input = e.target.value
        setSearchResults(null)

        if (input.length == 0) return
        setSearch(input)
    }

    function addState(nextStateNumber: number) {
        onAdd()
        addNextState(state.projectId, state.number, nextStateNumber, credentials)
            .then(() => onFinishAdd(true, null))
            .catch(err => onFinishAdd(false, err.message))
    }

    return (
        <div>
            <h1>Add Next State</h1>
            <input type="text" maxLength={64} placeholder="Search state" onChange={handleSearch} />
            <h4>{message}</h4>
            { searchResults ?
                <div>
                    <ul>
                        {
                            searchResults.states.map(state =>
                                <li key={state.id}>
                                    <p>Name: <Link to={`/projects/${state.projectId}/states/${state.number}`}>{state.name}</Link></p>
                                    <button onClick={() => addState(state.number)}>Add state</button>
                                </li>
                            )
                        }
                    </ul>
                    <hr></hr>
                </div>
                : <> </>
            }
        </div>
    )
}

export {
    SearchIssueStates
}