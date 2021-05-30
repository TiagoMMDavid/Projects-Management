import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { LabelItem } from './LabelItem'

type LabelsGetter = (projectId: number, page: number, credentials: Credentials) => Promise<Labels>

type LabelsPageProps = {
    getLabels: LabelsGetter
}

type LabelsPageParams = {
    projectId: string
}

type State = {
    state: 'has-labels' | 'loading-labels' | 'page-reset'
    labels: Labels
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-labels', labels: Labels } |
    { type: 'reset-page' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-labels', labels: null}
        case 'set-labels': return { state: 'has-labels', labels: action.labels }
        case 'reset-page': return { state: 'page-reset', labels: null }
    }
}

function LabelsPage({ getLabels }: LabelsPageProps): JSX.Element {
    const { projectId } = useParams<LabelsPageParams>()

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, labels }, dispatch] = useReducer(reducer, { state: 'page-reset', labels: null })
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getLabels(Number(projectId), page, ctx.credentials)
            .then(labels => {
                // TODO: Error handling
                dispatch({ type: 'set-labels', labels: labels})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let labelsView: JSX.Element
    switch(state) {
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
            {/* TODO: Create label */}
            { labelsView }
        </div>
    )
}

export {
    LabelsPage
}