import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../Paginated'
import { StateItem } from './StateItem'
import { CreateState } from './CreateState'
import { getProjectStates } from '../../api/states'
import { getSirenAction } from '../../api/apiRoutes'

type StatesPageParams = {
    projectId: string
}

type State = {
    state: 'has-states' | 'page-set' | 'message'
    states: IssueStates
    message: string
}
  
type Action =
    { type: 'set-states', states: IssueStates, message: string } |
    { type: 'set-page', message: string } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-states': return { state: 'has-states', states: action.states, message: action.message } as State
        case 'set-page': return { state: 'page-set', states: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message } as State
    }
}

function StatesPage(): JSX.Element {
    const { projectId } = useParams<StatesPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, states, message }, dispatch] = useReducer(reducer, { state: 'page-set' } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getProjectStates(Number(projectId), page, ctx.credentials)
                .then(states => {
                    if (isCancelled) return
                    dispatch({ type: 'set-states', states: states, message: message })
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

    let statesView: JSX.Element
    switch(state) {
        case 'message':
            statesView = <h1>{message}</h1>
            break
        case 'page-set':
            statesView = <h1>Loading states...</h1>
            break
        case 'has-states':
            statesView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(states.actions, 'create-state') != null ?
                        <CreateState
                            onFinishCreating={(success, message) => dispatch({ type: 'set-page', message: message, page: page } as Action)}
                            credentials={ctx.credentials} 
                            projectId={Number(projectId)}
                        />
                        : <> </>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={states.isLastPage} page={states.page}>
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
            { statesView }
        </div>
    )
}

export {
    StatesPage
}