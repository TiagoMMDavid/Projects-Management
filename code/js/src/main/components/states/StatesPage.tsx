import React from 'react'
import { Credentials } from '../../utils/userSession'

type StatesGetter = (projectId: number, page: number, credentials: Credentials) => Promise<States>

type StatesPageProps = {
    getStates: StatesGetter
}

function StatesPage({ getStates }: StatesPageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    StatesPage
}