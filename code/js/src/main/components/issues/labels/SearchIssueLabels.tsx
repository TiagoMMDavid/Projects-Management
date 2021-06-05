import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { addLabelToIssue, searchLabels } from '../../../api/labels'
import { Credentials } from '../../../utils/userSession'

type SearchIssueLabelsProps = {
    issue: Issue
    onFinishAdd: (success: boolean, message: string) => void
    credentials: Credentials
}


function SearchIssueLabels({ issue, onFinishAdd, credentials }: SearchIssueLabelsProps): JSX.Element {
    const [searchResults, setSearchResults] = useState<Labels>(null)
    const [search, setSearch] = useState<string>(null)
    const [toAdd, setToAdd] = useState<number>(null)
    const [message, setMessage] = useState(null)

    useEffect(() => {
        if (search && search.length > 0) {
            setMessage('Searching...')
            let isCancelled = false
            const tid = setTimeout(() => {
                searchLabels(issue.projectId, search, issue.id, credentials)
                    .then(labels => {
                        if (isCancelled) return

                        if (labels.labels.length == 0) return setMessage('No labels found')

                        setMessage(null)
                        setSearchResults(labels)
                    })
                    .catch(err => setMessage(err.message))
            }, 500)

            return () => {
                clearTimeout(tid)
                isCancelled = true
            }
        }
    }, [search])

    useEffect(() => {
        let isCancelled = false

        if (toAdd != null) {
            setMessage('Adding label...')
            addLabelToIssue(issue.projectId, issue.number, toAdd, credentials)
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
            <h1>Add Label</h1>
            <input type="text" maxLength={64} placeholder="Search label" onChange={handleSearch} />
            <h4>{message}</h4>
            { searchResults ?
                <div>
                    <ul>
                        {
                            searchResults.labels.map(label =>
                                <li key={label.id}>
                                    <p>Name: <Link to={`/projects/${issue.projectId}/labels/${label.number}`}>{label.name}</Link></p>
                                    <button onClick={() => setToAdd(label.number)}>Add label</button>
                                </li>
                            )
                        }
                    </ul>
                </div>
                : <> </>
            }
        </div>
    )
}

export {
    SearchIssueLabels
}