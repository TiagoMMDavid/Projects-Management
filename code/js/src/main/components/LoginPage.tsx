import React, { useEffect, useReducer, useRef, useState } from 'react'
import { Redirect } from 'react-router'
import { Credentials, generateCredentials, getUserCredentials, logIn } from '../utils/userSession'

type State = {
    state: undefined | 'loading' | 'valid' | 'invalid'
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-validation', isValid: boolean }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading' }
        case 'set-validation': return { state: action.isValid ? 'valid' : 'invalid'}  
    }
}

type Validator = (credentials: Credentials) => Promise<boolean>

type LoginPageProps = {
    redirectPath: string,
    validator: Validator
}

function LoginPage({ redirectPath, validator }: LoginPageProps): JSX.Element {
    const [{ state }, dispatch] = useReducer(reducer, { state: undefined })

    const userNameRef = useRef<HTMLInputElement>(null)
    const passwordRef = useRef<HTMLInputElement>(null)

    const isLoggedIn = getUserCredentials() != null

    function handleLogin() {
        
        const username = userNameRef.current?.value
        const password = passwordRef.current?.value

        console.log(`USERNAME: ${username} PASSWORD: ${password}`)

        const isValidUsername = username !== undefined && username.length != 0 && username.match(/\s/) == null
        const isValidPassword = username !== undefined && username.length != 0
        if (isValidUsername && isValidPassword) {
            dispatch({ type: 'set-loading' })

            const credentials = generateCredentials(username, password)
            console.log('Inside useEffect to log in')

            dispatch({ type: 'set-validation', isValid: true})

            validator(credentials)
                .then(isValid => {
                    if (isValid) {
                        logIn(username, password)
                    }
                    //dispatch({ type: 'set-validation', isValid: isValid})
                })
        }
    }

    console.log(state)
    return (
        state || isLoggedIn ? <Redirect to={redirectPath}></Redirect> :
            <div>
                Username: <input type='text' placeholder='username' ref={userNameRef}></input>
                <br></br>
                Password: <input type='password' placeholder='password' ref={passwordRef}></input>
                <br></br>
                <button onClick = {handleLogin}>LOGIN</button>
                <h1></h1>
            </div>        
    )
}

export {
    LoginPage
}