import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { addLabelToIssue, searchLabels } from '../../../api/labels'
import { Credentials } from '../../../utils/userSession'

type SearchIssueLabelsProps = {
    issue: Issue
    onAdd: () => void
    onFinishAdd: (success: boolean, message: string) => void
    credentials: Credentials
}


function SearchIssueLabels({ issue, onAdd, onFinishAdd, credentials }: SearchIssueLabelsProps): JSX.Element {
    const [searchResults, setSearchResults] = useState<Labels>(null)
    const [search, setSearch] = useState<string>(null)
    const [message, setMessage] = useState(null)

    useEffect(() => {
        if (search && search.length > 0) {
            setMessage('Searching...')
            let cancelled = false
            const tid = setTimeout(() => {
                searchLabels(issue.projectId, search, issue.id, credentials)
                    .then(labels => {
                        if (cancelled) return

                        if (labels.labels.length == 0) return setMessage('No labels found')

                        setMessage(null)
                        setSearchResults(labels)
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

    function addLabel(labelNumber: number) {
        onAdd()
        addLabelToIssue(issue.projectId, issue.number, labelNumber, credentials)
            .then(() => onFinishAdd(true, null))
            .catch(err => onFinishAdd(false, err.message))
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
                                    <button onClick={() => addLabel(label.number)}>Add label</button>
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
    SearchIssueLabels
}