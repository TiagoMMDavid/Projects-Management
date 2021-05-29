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

type Validator = (credentials: Credentials) => Promise<boolean>

type LoginPageProps = {
    redirectPath: string,
    validator: Validator
}

function LoginPage({ redirectPath, validator }: LoginPageProps): JSX.Element {
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
            validator(credentials)
                .then(isValid => {
                    if (isValid) ctx.logIn(username, password)
                    else dispatch({type: 'set-invalid'})
                })
        }
    }

    return (
        isLoggedIn ? <Redirect to={redirectPath}></Redirect> :
            <div>
                Username: <input type='text' placeholder='username' ref={userNameRef}></input>
                <br></br>
                Password: <input type='password' placeholder='password' ref={passwordRef}></input>
                <br></br>
                <button onClick = {handleLogin}>LOGIN</button>
                <h1>{state}</h1>
            </div>
    )
}

export {
    LoginPage
}