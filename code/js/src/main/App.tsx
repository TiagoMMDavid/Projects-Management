import React, { useState, useEffect } from 'react'
import { BrowserRouter as Router, Switch, Route, Link, useParams, Redirect } from 'react-router-dom'
import { ApiRoutes, fetchRoutes } from './api/apiRoutes'
import { validateUser } from './api/users'
import { LoginPage } from './components/LoginPage'
import { RequiresAuth } from './components/RequiresAuth'

const loginPage = '/login'

function LoadingPage() {
    return (
        <h1>Loading...</h1>
    )
}

function App() {

    //const isLoggedIn = userSession.getUserCredentials() != null
    const [resources, setResources] = useState<ApiRoutes>(null)

    useEffect(() => {
        if (!resources) {
            fetchRoutes()
                .then(apiResources => {
                    console.log(apiResources)
                    setResources(apiResources)
                })
        }
    }, [resources])

    return (
        <div className="App">
            <Router>
                <Switch>
                    <Route exact path={loginPage}>
                        {
                            !resources ? <LoadingPage /> :
                                <LoginPage redirectPath='/boas' validator={ validateUser }/> 
                        }
                    </Route>
                    
                    <Route exact path='/boas'>
                        <RequiresAuth loginPageRoute={loginPage}>
                            <h1>Hello world</h1>
                        </RequiresAuth>
                    </Route>
                    
                    <Route path='/'>
                        <Redirect to='/boas' />
                    </Route>
                </Switch>
            </Router>
        </div>
    )
}

export { App } 