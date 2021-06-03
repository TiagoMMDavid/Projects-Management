import React, { useContext, useEffect, useReducer } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { Credentials, UserContext } from '../../../utils/userSession'
import queryString from 'query-string'
import { Paginated } from '../../Paginated'
import { NextStateItem } from './NextStateItem'
import { SearchIssueStates } from './SearchStates'
import { getSirenAction } from '../../../api/apiRoutes'


type StatesPageProps = {
    getState: (projectId: number, stateNumber: number, credentials: Credentials) => Promise<IssueState>
    getNextStates: (projectId: number, stateNumber: number, credentials: Credentials, page: number) => Promise<IssueStates>
}

type StatesPageParams = {
    projectId: string,
    stateNumber: string,
}

type StateTransitions = {
    state: IssueState,
    nextStates: IssueStates
}

type State = {
    state: 'has-states' | 'loading-states' | 'page-reset' | 'hide' | 'message'
    stateTransitions: StateTransitions
    message: string
}
  
type Action =
    { type: 'set-loading', message: string } |
    { type: 'set-states', stateTransitions: StateTransitions, message: string } |
    { type: 'reset-page', message: string } |
    { type: 'hide'} | 
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-states', stateTransitions: null, message: action.message } as State
        case 'set-states': return { state: 'has-states', stateTransitions: action.stateTransitions, message: action.message } as State
        case 'reset-page': return { state: 'page-reset', stateTransitions: null, message: action.message } as State
        case 'set-message': return { state: 'message', message: action.message} as State
        case 'hide': return { state: 'hide' } as State
    }
}

function NextStatesPage({ getState, getNextStates }: StatesPageProps): JSX.Element {
    const { projectId, stateNumber } = useParams<StatesPageParams>()

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, stateTransitions, message }, dispatch] = useReducer(reducer, { state: 'page-reset' } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading', message: message})
        getState(Number(projectId), Number(stateNumber), ctx.credentials)
            .then(async state => {
                return {
                    state: state,
                    nextStates: state == null ? null : await getNextStates(Number(projectId), Number(stateNumber), ctx.credentials, page)
                } as StateTransitions
            })
            .then(stateTransitions => {
                if (!stateTransitions.state) dispatch({ type: 'set-message', message: 'State Not Found' })
                else if (!stateTransitions.nextStates) dispatch({ type: 'set-message', message: 'Error While Getting Next States' })
                else dispatch({type: 'set-states', stateTransitions: stateTransitions, message: message})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    console.log(stateTransitions)
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
                    <h4>{message}</h4>
                    {
                        getSirenAction(stateTransitions.nextStates.actions, 'create-next-state') != null ?
                            <SearchIssueStates
                                state={stateTransitions.state}
                                onAdd={() => dispatch({ type: 'set-message', message: 'Adding state...' })}
                                onFinishAdd={(success, message) => dispatch({ type: 'reset-page', message: message})}
                                credentials={ctx.credentials}
                            />
                            :
                            <></>
                    }
                    
                    <Paginated onChangePage={getPage} isLastPage={stateTransitions.nextStates.isLastPage} page={stateTransitions.nextStates.page}>
                        <h1>Next States</h1>
                        { stateTransitions.nextStates.states.length == 0 ? 
                            <p> No next states found </p> :
                            <ul>
                                {stateTransitions.nextStates.states.map((state: IssueState) => 
                                    <NextStateItem
                                        key={state.id} 
                                        state={stateTransitions.state} 
                                        nextState={state}
                                        onRemove={() => dispatch({ type: 'set-message', message: 'Removing state...' })}
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
            <Link to={`/projects/${projectId}/states/${stateNumber}`}>Back to state</Link>
            { statesView }
        </div>
    )
}

export {
    NextStatesPage
}