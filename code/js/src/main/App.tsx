import React, { useState, useEffect } from 'react'
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom'
import { ApiRoutes, fetchRoutes } from './api/apiRoutes'
import { validateUser } from './api/users'
import { Header } from './components/Header'
import { LoginPage } from './components/LoginPage'
import { ProjectsPage } from './components/ProjectsPage'
import { RequiresAuth } from './components/RequiresAuth'
import { UserContext, Credentials, getInitialContext, getStoredCredentials } from './utils/userSession'

const LOGIN_PATH = '/login'
const PROJECTS_PATH = '/projects'

function LoadingPage() {
    return (
        <h1>Loading...</h1>
    )
}

function App(): JSX.Element {
    const [resources, setResources] = useState<ApiRoutes>(null)
    const [userCredentials, setUserCredentials] = useState<Credentials>(getStoredCredentials())

    useEffect(() => {
        if (!resources) {
            fetchRoutes()
                .then(apiResources => setResources(apiResources))
        }
    }, [resources])

    return (
        <UserContext.Provider value={getInitialContext(userCredentials, setUserCredentials)}>
            <Router>
                <Header></Header>
                <Switch>
                    <Route exact path={LOGIN_PATH}>
                        {
                            !resources ? <LoadingPage /> :
                                <LoginPage redirectPath='/' validator={ validateUser }/> 
                        }
                    </Route>

                    <Route exact path={PROJECTS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <ProjectsPage />
                            }
                        </RequiresAuth>
                    </Route>
                    
                    <Route path='/'>
                        <Redirect to={PROJECTS_PATH} />
                    </Route>
                </Switch>
            </Router>
        </UserContext.Provider>
    )
}

export { App } 