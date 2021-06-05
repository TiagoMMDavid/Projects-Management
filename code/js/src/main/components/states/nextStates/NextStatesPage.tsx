import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { UserContext } from '../../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../../Paginated'
import { NextStateItem } from './NextStateItem'
import { SearchIssueStates } from './SearchStates'
import { getSirenAction } from '../../../api/apiRoutes'
import { getNextStates, getState } from '../../../api/states'

type StatesPageParams = {
    projectId: string,
    stateNumber: string,
}

type StateTransitions = {
    state: IssueState,
    nextStates: IssueStates
}

type State = {
    state: 'has-states' | 'page-set' | 'message'
    stateTransitions: StateTransitions
    message: string
}
  
type Action =
    { type: 'set-states', stateTransitions: StateTransitions, message: string } |
    { type: 'set-page', message: string } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-states': return { state: 'has-states', stateTransitions: action.stateTransitions, message: action.message } as State
        case 'set-page': return { state: 'page-set', stateTransitions: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message } as State
    }
}

function NextStatesPage(): JSX.Element {
    const { projectId, stateNumber } = useParams<StatesPageParams>()

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, stateTransitions, message }, dispatch] = useReducer(reducer, { state: 'page-set' } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getState(Number(projectId), Number(stateNumber), ctx.credentials)
                .then(async state => {
                    if (isCancelled) return null

                    return {
                        state: state,
                        nextStates: await getNextStates(Number(projectId), Number(stateNumber), ctx.credentials, page)
                    } as StateTransitions
                })
                .then(stateTransitions => {
                    if (isCancelled) return
                    dispatch({type: 'set-states', stateTransitions: stateTransitions, message: message})
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
                    {
                        getSirenAction(stateTransitions.nextStates.actions, 'add-next-state') != null ?
                            <SearchIssueStates
                                state={stateTransitions.state}
                                onFinishAdd={(success, message) => dispatch({ type: 'set-page', message: message})}
                                credentials={ctx.credentials}
                            />
                            :
                            <></>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={stateTransitions.nextStates.isLastPage} page={stateTransitions.nextStates.page}>
                        <h1>Next States</h1>
                        { stateTransitions.nextStates.states.length == 0 ? 
                            <p> No next states found </p> :
                            <ul>
                                {stateTransitions.nextStates.states.map((state: IssueState) => 
                                    <NextStateItem
                                        key={state.id} 
                                        state={stateTransitions.state} 
                                        nextState={state}
                                        onFinishRemove={(success, message) => dispatch({ type: 'set-page', message: message})}
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
            <Link to={`/projects/${projectId}/states/${stateNumber}`}>{'<< Back to state'}</Link>
            { statesView }
        </div>
    )
}

export {
    NextStatesPage
}