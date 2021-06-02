import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import { Paginated } from '../Paginated'
import queryString from 'query-string'
import { UserItem } from './UserItem'

type UsersGetter = (page: number, credentials: Credentials) => Promise<Users>

type UsersPageProps = {
    getUsers: UsersGetter
}

type State = {
    state: 'has-users' | 'loading-users' | 'page-reset'
    users: Users
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-users', users: Users } |
    { type: 'reset-page' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-users', users: null}
        case 'set-users': return { state: 'has-users', users: action.users }
        case 'reset-page': return { state: 'page-reset', users: null }
    }
}

function UsersPage({ getUsers }: UsersPageProps): JSX.Element {

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, users }, dispatch] = useReducer(reducer, { state: 'page-reset', users: null })
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getUsers(page, ctx.credentials)
            .then(users => {
                dispatch({ type: 'set-users', users: users})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    switch(state) {
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