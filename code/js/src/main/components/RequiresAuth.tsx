import React from 'react'
import { ReactNode } from 'react'
import { Redirect } from 'react-router-dom'
import { UserContext } from '../utils/userSession'

type EnsureCredentialsProps = {
    loginPageRoute: string,
    children?: ReactNode
  }

function RequiresAuth({ loginPageRoute, children }: EnsureCredentialsProps): JSX.Element {
    return (
        <UserContext.Consumer>
            {ctx => ctx?.credentials ? children : <Redirect to={ loginPageRoute } /> }
        </UserContext.Consumer>
    )
}

export {
    RequiresAuth
}