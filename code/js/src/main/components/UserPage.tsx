import React, { useContext, useEffect, useReducer, useState } from 'react'
import { useParams } from 'react-router'
import { Credentials, UserContext } from '../utils/userSession'

type UserPageProps = {
    getUser: (userId: number, credentials: Credentials) => Promise<User>
}
type UserProps = {
    user: User
}

type UserPageParams = {
    userId: string
}

function User({ user }: UserProps): JSX.Element {
    return (
        <div>
            <p>Name: {user.name}</p>
            <p>Id: {user.id}</p>
        </div>
    )
}

type State = {
    state: 'has-user' | 'loading-user' | 'no-user'
    user: User
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-user', user: User} |
    { type: 'set-no-user' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-user', user: null}
        case 'set-user': return { state: 'has-user', user: action.user}
        case 'set-no-user': return { state: 'no-user', user: null}
    }
}

function UserPage({ getUser }: UserPageProps): JSX.Element {
    const { userId } = useParams<UserPageParams>()
    const [{ state, user }, dispatch] = useReducer(reducer, {state: 'loading-user', user: null})
    const ctx = useContext(UserContext)

    useEffect(() => {
        getUser(Number(userId), ctx.credentials)
            .then(user => {
                if (user) dispatch({ type: 'set-user', user: user })
                else dispatch({ type: 'set-no-user' })
            })
    }, [])

    switch(state) {
        case 'loading-user': 
            return (
                <h1> Loading user... </h1> 
            )
        case 'has-user':
            return (
                <User user={user}/>
            )
        case 'no-user':
            return (
                <h1> User not found </h1> 
            )
    }
}

export {
    UserPage
}