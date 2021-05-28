import React from 'react'
import { ReactNode } from 'react'
import { Redirect } from 'react-router-dom'
import { getUserCredentials } from '../utils/userSession'

type EnsureCredentialsProps = {
    loginPageRoute: string,
    children?: ReactNode
  }

function RequiresAuth({ loginPageRoute, children }: EnsureCredentialsProps): JSX.Element {
    const isLoggedIn = getUserCredentials() != null

    return (
        isLoggedIn ? <> { children } </>: <Redirect to={ loginPageRoute } />
    )
}

export {
    RequiresAuth
}