import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { LabelItem } from './LabelItem'
import { CreateLabel } from './CreateLabel'
import { getProjectLabels } from '../../api/labels'
import { getSirenAction } from '../../api/apiRoutes'

type LabelsPageParams = {
    projectId: string
}

type State = {
    state: 'has-labels' | 'page-set' | 'message'
    labels: Labels
    message: string
}
  
type Action =
    { type: 'set-labels', labels: Labels, message: string } |
    { type: 'set-page', message: string } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-labels': return { state: 'has-labels', labels: action.labels, message: action.message } as State
        case 'set-page': return { state: 'page-set', labels: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
    }
}

function LabelsPage(): JSX.Element {
    const { projectId } = useParams<LabelsPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, labels, message }, dispatch] = useReducer(reducer, { state: 'page-set' } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getProjectLabels(Number(projectId), page, ctx.credentials)
                .then(labels => {
                    if (isCancelled) return
                    dispatch({ type: 'set-labels', labels: labels, message: message})
                })
                .catch(err => {
                    if (isCancelled) return
                    dispatch({ type: 'set-message', message: err.message })
                })
        }

        return () => {
            isCancelled = true
        }
    }, [state])

    let labelsView: JSX.Element
    switch(state) {
        case 'message':
            labelsView = <h1>{message}</h1>
            break
        case 'page-set':
            labelsView = <h1>Loading labels...</h1>
            break
        case 'has-labels':
            labelsView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(labels.actions, 'create-label') != null ?
                        <CreateLabel
                            onFinishCreating={(success, message) => dispatch({ type: 'set-page', message: message, page: page } as Action)} 
                            credentials={ctx.credentials} 
                            projectId={Number(projectId)}
                        />
                        : <> </>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={labels.isLastPage} page={labels.page}>
                        <h1>Labels</h1>
                        { labels.labels.length == 0 ? 
                            <p> No labels found </p> :
                            <ul>
                                {labels.labels.map((label: Label) => <LabelItem key={label.id} label={label} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}`}>{'<< Back to project'}</Link>
            { labelsView }
        </div>
    )
}

export {
    LabelsPage
}