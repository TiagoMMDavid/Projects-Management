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
    currUserId: number
    message: string
}
  
type Action =
    { type: 'set-loading', currUserId: number } |
    { type: 'set-user', user: User} |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-user', user: null, currUserId: action.currUserId } as State
        case 'set-user': return { state: 'has-user', user: action.user, currUserId: action.user.id } as State
        case 'set-message': return { state: 'message', message: action.message, currUserId: state.currUserId } as State
    }
}

function UserPage(): JSX.Element {
    const { userId } = useParams<UserPageParams>()
    const [{ state, user, currUserId, message }, dispatch] = useReducer(reducer, { state: 'loading-user', user: null } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        const id = Number(userId)

        if (!isNaN(id) && currUserId != id) {
            dispatch({type: 'set-loading', currUserId: id})
        }

        if (state == 'loading-user') {
            getUser(id, ctx.credentials)
                .then(user => {
                    if (isCancelled) return
                    dispatch({ type: 'set-user', user: user })
                })
                .catch(err => {
                    if (isCancelled) return
                    dispatch({ type: 'set-message', message: err.message })
                })
        }
        
        return () => {
            isCancelled = true
        }
    }, [state, userId])

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
                <div>
                    <h1>User</h1>
                    <User user={user}/>
                </div>
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