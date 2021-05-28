import React, { useState, useEffect } from 'react'
import { BrowserRouter as Router, Switch, Route, Link, useParams, Redirect } from 'react-router-dom'
import { ApiRoutes, fetchRoutes } from './api/apiRoutes'
import { validateUser } from './api/users'
import { LoginPage } from './components/LoginPage'
import { RequiresAuth } from './components/RequiresAuth'

const LOGIN_PATH = '/login'
const PROJECTS_PATH = '/projects'

function LoadingPage() {
    return (
        <h1>Loading...</h1>
    )
}

function App(): JSX.Element {
    const [resources, setResources] = useState<ApiRoutes>(null)

    useEffect(() => {
        if (!resources) {
            fetchRoutes()
                .then(apiResources => setResources(apiResources))
        }
    }, [resources])

    return (
        <div className="App">
            <Router>
                <Switch>
                    <Route exact path={LOGIN_PATH}>
                        {
                            !resources ? <LoadingPage /> :
                                <LoginPage redirectPath='/' validator={ validateUser }/> 
                        }
                    </Route>
                    
                    <Route exact path={PROJECTS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            <h1>Hello world</h1>
                        </RequiresAuth>
                    </Route>
                    
                    <Route path='/'>
                        <Redirect to={PROJECTS_PATH} />
                    </Route>
                </Switch>
            </Router>
        </div>
    )
}

export { App } 