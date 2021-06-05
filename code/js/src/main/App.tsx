import React, { useState, useEffect } from 'react'
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom'

import { UserContext, Credentials, getInitialContext, getStoredCredentials } from './utils/userSession'
import { ApiRoutes, fetchRoutes } from './api/apiRoutes'
import { getAuthUser } from './api/users'

import { RequiresAuth } from './components/RequiresAuth'
import { Navbar } from './components/Navbar'
import { LoginPage } from './components/LoginPage'
import { ProjectsPage } from './components/projects/ProjectsPage'
import { ProjectPage } from './components/projects/ProjectPage'
import { LabelsPage } from './components/labels/LabelsPage'
import { LabelPage } from './components/labels/LabelPage'
import { StatesPage } from './components/states/StatesPage'
import { StatePage } from './components/states/StatePage'
import { IssuesPage } from './components/issues/IssuesPage'
import { IssuePage } from './components/issues/IssuePage'
import { CommentsPage } from './components/comments/CommentsPage'
import { CommentPage } from './components/comments/CommentPage'
import { UserPage } from './components/users/UserPage'
import { UsersPage } from './components/users/UsersPage'
import { IssueLabelsPage } from './components/issues/labels/IssueLabelsPage'
import { NextStatesPage } from './components/states/nextStates/NextStatesPage'
import { LoadingPage } from './components/LoadingPage'


const LOGIN_PATH = '/login'

const PROJECTS_PATH = '/projects'
const PROJECT_PATH = `${PROJECTS_PATH}/:projectId`

const LABELS_PATH = `${PROJECT_PATH}/labels`
const LABEL_PATH = `${LABELS_PATH}/:labelNumber`

const STATES_PATH = `${PROJECT_PATH}/states`
const STATE_PATH = `${STATES_PATH}/:stateNumber`
const NEXT_STATES_PATH = `${STATE_PATH}/nextStates`

const ISSUES_PATH = `${PROJECT_PATH}/issues`
const ISSUE_PATH = `${ISSUES_PATH}/:issueNumber`
const ISSUE_LABELS_PATH = `${ISSUE_PATH}/labels`

const COMMENTS_PATH = `${ISSUE_PATH}/comments`
const COMMENT_PATH = `${COMMENTS_PATH}/:commentNumber`

const USERS_PATH = '/users'
const USER_PATH = `${USERS_PATH}/:userId`


const LOADING_MESSAGE = 'Loading...'
const MAX_NUMBER_OF_RETRIES = 5

function NotFound() {
    return (
        <h1>Not Found</h1>
    )
}

function App(): JSX.Element {
    const [resources, setResources] = useState<ApiRoutes>(null)
    const [retries, setRetries] = useState(MAX_NUMBER_OF_RETRIES)
    const [message, setMessage] = useState(LOADING_MESSAGE)
    const [userCredentials, setUserCredentials] = useState<Credentials>(getStoredCredentials())

    useEffect(() => {
        if (retries <= 0) {
            return setMessage('Failed to load App. Please try again later')
        }

        if (!resources) {
            fetchRoutes()
                .then(apiResources => {
                    if (!apiResources) setRetries(retries - 1)
                    else setResources(apiResources)
                })
        }
    }, [resources, retries])

    return (
        <UserContext.Provider value={getInitialContext(userCredentials, setUserCredentials)}>
            <Router>
                <Navbar/>
                <div className="content">
                    <Switch>
                        <Route exact path={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0} /> :
                                    <LoginPage redirectPath='/' getAuthUser={ getAuthUser }/> 
                            }
                        </Route>

                        {/* Projects Routes*/}
                        <Route exact path={PROJECTS_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <ProjectsPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={PROJECT_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <ProjectPage/>
                                }
                            </RequiresAuth>
                        </Route>

                        {/* Labels Routes*/}
                        <Route exact path={LABELS_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <LabelsPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={LABEL_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <LabelPage/>
                                }
                            </RequiresAuth>
                        </Route>

                        {/* States Routes*/}
                        <Route exact path={STATES_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <StatesPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={STATE_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <StatePage/>
                                }
                            </RequiresAuth>
                        </Route>

                        <Route exact path={NEXT_STATES_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <NextStatesPage/>
                                }
                            </RequiresAuth>
                        </Route>

                        {/* Issues Routes*/}
                        <Route exact path={ISSUES_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <IssuesPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={ISSUE_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <IssuePage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={ISSUE_LABELS_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <IssueLabelsPage/>
                                }
                            </RequiresAuth>
                        </Route>

                        {/* Comments Routes*/}
                        <Route exact path={COMMENTS_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <CommentsPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={COMMENT_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <CommentPage/>
                                }
                            </RequiresAuth>
                        </Route>

                        {/* Users Routes*/}
                        <Route exact path={USERS_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <UsersPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        <Route exact path={USER_PATH}>
                            <RequiresAuth loginPageRoute={LOGIN_PATH}>
                                {
                                    !resources ? <LoadingPage loadingMsg={message} loadFailed={retries <= 0}/> :
                                        <UserPage/>
                                }
                            </RequiresAuth>
                        </Route>
                        
                        {/* 404 */}
                        <Route exact path='/'>
                            <Redirect to={PROJECTS_PATH} />
                        </Route>
                        <Route component={NotFound} />
                    </Switch>
                </div>
            </Router>
        </UserContext.Provider>
    )
}

export { App } 