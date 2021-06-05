import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { addNextState, searchStates } from '../../../api/states'
import { Credentials } from '../../../utils/userSession'

type SearchStatesProps = {
    state: IssueState
    onFinishAdd: (success: boolean, message: string) => void
    credentials: Credentials
}

function SearchIssueStates({ state, onFinishAdd, credentials }: SearchStatesProps): JSX.Element {
    const [searchResults, setSearchResults] = useState<IssueStates>(null)
    const [search, setSearch] = useState<string>(null)
    const [toAdd, setToAdd] = useState<number>(null)
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

    useEffect(() => {
        let isCancelled = false

        if (toAdd != null) {
            setMessage('Adding state...')
            addNextState(state.projectId, state.number, toAdd, credentials)
                .then(() => {
                    if (isCancelled) return
                    onFinishAdd(true, null)
                })
                .catch(err => {
                    if (isCancelled) return
                    onFinishAdd(false, err.message)
                })
        }

        return () => {
            isCancelled = true
        }
    }, [toAdd])

    function handleSearch(e: React.ChangeEvent<HTMLInputElement>) {
        const input = e.target.value
        setSearchResults(null)

        if (input.length == 0) return
        setSearch(input)
    }

    return (
        <div>
            <h1>Add Next State</h1>
            <input type="text" maxLength={64} placeholder="Search state" onChange={handleSearch} />
            <h4>{message}</h4>
            { searchResults ?
                <ul>
                    {
                        searchResults.states.map(state =>
                            <li key={state.id}>
                                <p>Name: <Link to={`/projects/${state.projectId}/states/${state.number}`}>{state.name}</Link></p>
                                <button onClick={() => setToAdd(state.number)}>Add state</button>
                            </li>
                        )
                    }
                </ul>
                : <> </>
            }
        </div>
    )
}

export {
    SearchIssueStates
}