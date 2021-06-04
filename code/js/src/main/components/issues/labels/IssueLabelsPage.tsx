import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../../Paginated'
import { IssueLabelItem } from './IssueLabelItem'
import { SearchIssueLabels } from './SearchIssueLabels'
import { getIssue } from '../../../api/issues'
import { getIssueLabels } from '../../../api/labels'

type LabelsPageParams = {
    projectId: string,
    issueNumber: string,
}

type IssueLabels = {
    issue: Issue,
    labels: Labels
}

type State = {
    state: 'has-labels' | 'loading-labels' | 'page-reset' | 'hide' | 'message'
    issueLabels: IssueLabels
    message: string
}
  
type Action =
    { type: 'set-loading', message: string } |
    { type: 'set-labels', issueLabels: IssueLabels, message: string } |
    { type: 'reset-page', message: string } |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-labels', issueLabels: null, message: action.message } as State
        case 'set-labels': return { state: 'has-labels', issueLabels: action.issueLabels, message: action.message } as State
        case 'reset-page': return { state: 'page-reset', issueLabels: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function IssueLabelsPage(): JSX.Element {
    const { projectId, issueNumber } = useParams<LabelsPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, issueLabels, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading', message: message})
        getIssue(Number(projectId), Number(issueNumber), ctx.credentials)
            .then(async issue => {
                return {
                    issue: issue,
                    labels: await getIssueLabels(Number(projectId), Number(issueNumber), page, ctx.credentials)
                } as IssueLabels
            })
            .then(issueLabels => dispatch({type: 'set-labels', issueLabels: issueLabels, message: message}))
            .catch(err => dispatch({ type: 'set-message', message: err.message }))
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let labelsView: JSX.Element
    switch(state) {
        case 'message':
            labelsView = <h1>{message}</h1>
            break
        case 'hide':
            break
        case 'page-reset':
        case 'loading-labels':
            labelsView = <h1>Loading labels...</h1>
            break
        case 'has-labels':
            labelsView = 
                <div>
                    <h4>{message}</h4>
                    <SearchIssueLabels
                        issue={issueLabels.issue}
                        onAdd={() => dispatch({ type: 'set-message', message: 'Adding label...' })}
                        onFinishAdd={(success, message) => dispatch({ type: 'reset-page', message: message})}
                        credentials={ctx.credentials}
                    />
                    <Paginated onChangePage={getPage} isLastPage={issueLabels.labels.isLastPage} page={issueLabels.labels.page}>
                        <h1>Labels</h1>
                        { issueLabels.labels.labels.length == 0 ? 
                            <p> No labels found </p> :
                            <ul>
                                {issueLabels.labels.labels.map((label: Label) => 
                                    <IssueLabelItem 
                                        key={label.id} 
                                        label={label} 
                                        issue={issueLabels.issue}
                                        onRemove={() => dispatch({ type: 'set-message', message: 'Removing label...' })}
                                        onFinishRemove={(success, message) => dispatch({ type: 'reset-page', message: message})}
                                        credentials={ctx.credentials}
                                    />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}/issues/${issueNumber}`}>{'<< Back to issue'}</Link>
            { labelsView }
        </div>
    )
}

export {
    IssueLabelsPage
}