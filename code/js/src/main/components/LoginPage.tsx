import React, { useContext, useReducer, useRef } from 'react'
import { Redirect } from 'react-router'
import { Credentials, generateCredentials, UserContext } from '../utils/userSession'

type State = {
    state: undefined | 'Logging in...' | 'Invalid credentials'
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-invalid' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'Logging in...' }
        case 'set-invalid': return { state: 'Invalid credentials' }  
    }
}

type AuthUserGetter = (credentials: Credentials) => Promise<User>

type LoginPageProps = {
    redirectPath: string,
    getAuthUser: AuthUserGetter
}

function LoginPage({ redirectPath, getAuthUser }: LoginPageProps): JSX.Element {
    const [{ state }, dispatch] = useReducer(reducer, { state: undefined })
    const ctx = useContext(UserContext)
    const isLoggedIn = ctx?.credentials != null

    const userNameRef = useRef<HTMLInputElement>(null)
    const passwordRef = useRef<HTMLInputElement>(null)

    function handleLogin() {
        const username = userNameRef.current?.value
        const password = passwordRef.current?.value

        const isValidUsername = username !== undefined && username.length != 0 && username.match(/\s/) == null
        const isValidPassword = username !== undefined && username.length != 0
        if (isValidUsername && isValidPassword) {
            dispatch({ type: 'set-loading' })

            const credentials = generateCredentials(username, password)
            getAuthUser(credentials)
                .then(user => ctx.logIn(username, password, user.id))
                .catch(() => dispatch({type: 'set-invalid'}))
        } else {
            dispatch({type: 'set-invalid'})
        }
    }

    return (
        isLoggedIn ? <Redirect to={redirectPath}></Redirect> :
            <div className="center login">
                <h2>Username</h2>
                <input type='text' placeholder='username' className="mt-2" ref={userNameRef}></input>
                <h2 className="mt-3">Password</h2>
                <input type='password' placeholder='password' className="mt-2" ref={passwordRef}></input>
                <button className="mt-3" onClick={handleLogin}>Login</button>
                <h1>{state}</h1>
            </div>
    )
}

export {
    LoginPage
}