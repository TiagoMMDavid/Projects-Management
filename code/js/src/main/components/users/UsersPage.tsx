import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import { Paginated } from '../Paginated'
import queryString from 'query-string'
import { UserItem } from './UserItem'
import { getUsers } from '../../api/users'

type State = {
    state: 'has-users' | 'loading-users' | 'page-reset' | 'message'
    users: Users
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-users', users: Users } |
    { type: 'reset-page' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-users', users: null} as State
        case 'set-users': return { state: 'has-users', users: action.users } as State
        case 'reset-page': return { state: 'page-reset', users: null } as State
        case 'set-message': return { state: 'message', message: action.message } as State
    }
}

function UsersPage(): JSX.Element {

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, users, message }, dispatch] = useReducer(reducer, { state: 'page-reset', users: null } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getUsers(page, ctx.credentials)
            .then(users => dispatch({ type: 'set-users', users: users}))
            .catch(err => dispatch({ type: 'set-message', message: err.message }))
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    switch(state) {
        case 'message':
            return <h1>{message}</h1>
        case 'page-reset':
        case 'loading-users':
            return <h1>Loading users...</h1>
            
        case 'has-users':
            return (
                <div>
                    <Paginated onChangePage={getPage} isLastPage={users.isLastPage} page={users.page}>
                        <h1>Users</h1>
                        { users.users.length == 0 ? 
                            <p> No users found </p> :
                            <ul>
                                {users.users.map((user: User) => <UserItem key={user.id} user={user} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            )
    }
}

export {
    UsersPage
}