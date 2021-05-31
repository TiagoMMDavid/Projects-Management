import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { LabelItem } from './LabelItem'
import { CreateLabel } from './CreateLabel'

type LabelsGetter = (projectId: number, page: number, credentials: Credentials) => Promise<Labels>

type LabelsPageProps = {
    getLabels: LabelsGetter
}

type LabelsPageParams = {
    projectId: string
}

type State = {
    state: 'has-labels' | 'loading-labels' | 'page-reset' | 'hide' | 'message'
    labels: Labels
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-labels', labels: Labels } |
    { type: 'reset-page' } |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-labels', labels: null } as State
        case 'set-labels': return { state: 'has-labels', labels: action.labels } as State
        case 'reset-page': return { state: 'page-reset', labels: null } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function LabelsPage({ getLabels }: LabelsPageProps): JSX.Element {
    const { projectId } = useParams<LabelsPageParams>()

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, labels, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getLabels(Number(projectId), page, ctx.credentials)
            .then(labels => {
                if (!labels) dispatch({ type: 'set-message', message: 'Failed to get labels' } as Action)
                else dispatch({ type: 'set-labels', labels: labels})
            })
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
                    <Paginated onChangePage={getPage} isLastPage={labels.isLastPage} page={labels.page}>
                        <h1>Labels</h1>
                        { labels.labels.length == 0 ? <p> No labels found </p> :
                            labels.labels.map((label: Label) => <LabelItem key={label.id} label={label} />)}
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}`}>Back to project</Link>
            <CreateLabel
                onFinishCreating={() => dispatch({type: 'reset-page'})} 
                onCreating={() => dispatch({type: 'hide'})}
                credentials={ctx.credentials} 
                projectId={Number(projectId)}
            />
            { labelsView }
        </div>
    )
}

export {
    LabelsPage
}