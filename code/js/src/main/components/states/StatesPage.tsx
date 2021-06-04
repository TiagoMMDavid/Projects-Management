import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { StateItem } from './StateItem'
import { CreateState } from './CreateState'
import { getProjectStates } from '../../api/states'

type StatesPageParams = {
    projectId: string
}

type State = {
    state: 'has-states' | 'loading-states' | 'page-reset' | 'hide' | 'message'
    states: IssueStates
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-states', states: IssueStates } |
    { type: 'reset-page' } |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-states', states: null } as State
        case 'set-states': return { state: 'has-states', states: action.states } as State
        case 'reset-page': return { state: 'page-reset', states: null } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function StatesPage(): JSX.Element {
    const { projectId } = useParams<StatesPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, states, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getProjectStates(Number(projectId), page, ctx.credentials)
            .then(states => dispatch({ type: 'set-states', states: states }))
            .catch(err => dispatch({ type: 'set-message', message: err.message }))
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let statesView: JSX.Element
    switch(state) {
        case 'message':
            statesView = <h1>{message}</h1>
            break
        case 'hide':
            break
        case 'page-reset':
        case 'loading-states':
            statesView = <h1>Loading states...</h1>
            break
        case 'has-states':
            statesView = 
                <div>
                    <Paginated onChangePage={getPage} isLastPage={states.isLastPage} page={states.page}>
                        <h1>States</h1>
                        { states.states.length == 0 ? 
                            <p> No states found </p> :
                            <ul>
                                {states.states.map((state: IssueState) => <StateItem key={state.id} state={state} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            <Link to={`/projects/${projectId}`}>{'<< Back to project'}</Link>
            <CreateState
                onFinishCreating={() => dispatch({type: 'reset-page'})} 
                onCreating={() => dispatch({type: 'hide'})}
                credentials={ctx.credentials} 
                projectId={Number(projectId)}
            />
            { statesView }
        </div>
    )
}

export {
    StatesPage
}