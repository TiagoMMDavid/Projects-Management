import React from 'react'
import { Credentials } from '../../utils/userSession'

type StatePageProps = {
    getState: (projectId: number, stateNumber: number, credentials: Credentials) => Promise<State>
}

function StatePage({ getState }: StatePageProps): JSX.Element {
    //TODO:
    return (
        <h1> TODO </h1>
    )
}

export {
    StatePage
}