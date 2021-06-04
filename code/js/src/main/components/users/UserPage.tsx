import React, { useContext, useEffect, useReducer } from 'react'
import { useParams } from 'react-router'
import { Link } from 'react-router-dom'
import { getUser } from '../../api/users'
import { UserContext } from '../../utils/userSession'

type UserProps = {
    user: User
}

type UserPageParams = {
    userId: string
}

function User({ user }: UserProps): JSX.Element {
    return (
        <div>
            <p>Id: {user.id}</p>
            <p>Name: {user.name}</p>
        </div>
    )
}

type State = {
    state: 'has-user' | 'loading-user' | 'message'
    user: User
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-user', user: User} |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-user', user: null } as State
        case 'set-user': return { state: 'has-user', user: action.user } as State
        case 'set-message': return { state: 'message', message: action.message } as State
    }
}

function UserPage(): JSX.Element {
    const { userId } = useParams<UserPageParams>()
    const [{ state, user, message }, dispatch] = useReducer(reducer, { state: 'loading-user', user: null } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        getUser(Number(userId), ctx.credentials)
            .then(user => dispatch({ type: 'set-user', user: user }))
            .catch(err => dispatch({ type: 'set-message', message: err.message }))
    }, [userId])

    let toReturn: JSX.Element
    switch(state) {
        case 'message':
            toReturn = (
                <h1>{message}</h1> 
            )
            break
        case 'loading-user': 
            toReturn = (
                <h1>Loading user...</h1> 
            )
            break
        case 'has-user':
            toReturn = (
                <User user={user}/>
            )
            break
    }
    return (
        <div>
            <Link to="/users">{'<< View all users'}</Link>
            {toReturn}
        </div>
    )
}

export {
    UserPage
}