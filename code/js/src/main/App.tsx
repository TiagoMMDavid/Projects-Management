import React, { useState, useEffect } from 'react'
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom'

import { UserContext, Credentials, getInitialContext, getStoredCredentials } from './utils/userSession'
import { ApiRoutes, fetchRoutes } from './api/apiRoutes'
import { getProject, getProjects } from './api/projects'
import { getUser, getAuthUser, getUsers } from './api/users'

import { RequiresAuth } from './components/RequiresAuth'
import { Header } from './components/Header'
import { LoginPage } from './components/LoginPage'
import { ProjectsPage } from './components/projects/ProjectsPage'
import { ProjectPage } from './components/projects/ProjectPage'
import { getIssueLabels, getLabel, getProjectLabels, searchLabels } from './api/labels'
import { getNextStates, getState, getStates } from './api/states'
import { getIssue, getIssues } from './api/issues'
import { getComment, getComments } from './api/comments'
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


const LOGIN_PATH = '/login'

const PROJECTS_PATH = '/projects'
const PROJECT_PATH = `${PROJECTS_PATH}/:projectId`

const LABELS_PATH = `${PROJECT_PATH}/labels`
const LABEL_PATH = `${LABELS_PATH}/:labelNumber`

const STATES_PATH = `${PROJECT_PATH}/states`
const STATE_PATH = `${STATES_PATH}/:stateNumber`

const ISSUES_PATH = `${PROJECT_PATH}/issues`
const ISSUE_PATH = `${ISSUES_PATH}/:issueNumber`
const ISSUE_LABELS_PATH = `${ISSUE_PATH}/labels`

const COMMENTS_PATH = `${ISSUE_PATH}/comments`
const COMMENT_PATH = `${COMMENTS_PATH}/:commentNumber`

const USERS_PATH = '/users'
const USER_PATH = `${USERS_PATH}/:userId`


function LoadingPage() {
    return (
        <h1>Loading...</h1>
    )
}

function NotFound() {
    return (
        <h1>Not Found</h1>
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
                                <LoginPage redirectPath='/' getAuthUser={ getAuthUser }/> 
                        }
                    </Route>

                    {/* Projects Routes*/}
                    <Route exact path={PROJECTS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <ProjectsPage getProjects={ getProjects }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={PROJECT_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <ProjectPage getProject={ getProject }/>
                            }
                        </RequiresAuth>
                    </Route>

                    {/* Labels Routes*/}
                    <Route exact path={LABELS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <LabelsPage getLabels={ getProjectLabels }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={LABEL_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <LabelPage getLabel={ getLabel }/>
                            }
                        </RequiresAuth>
                    </Route>

                    {/* States Routes*/}
                    <Route exact path={STATES_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <StatesPage getStates={ getStates }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={STATE_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <StatePage getState={ getState }/>
                            }
                        </RequiresAuth>
                    </Route>

                    {/* Issues Routes*/}
                    <Route exact path={ISSUES_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <IssuesPage getIssues={ getIssues }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={ISSUE_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <IssuePage getIssue={ getIssue } getNextStates={ getNextStates }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={ISSUE_LABELS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <IssueLabelsPage getIssue={ getIssue } getIssueLabels={ getIssueLabels } searchLabels={ searchLabels }/>
                            }
                        </RequiresAuth>
                    </Route>

                    {/* Comments Routes*/}
                    <Route exact path={COMMENTS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <CommentsPage getComments={ getComments }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={COMMENT_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <CommentPage getComment={ getComment }/>
                            }
                        </RequiresAuth>
                    </Route>

                    {/* Users Routes*/}
                    <Route exact path={USERS_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <UsersPage getUsers={ getUsers }/>
                            }
                        </RequiresAuth>
                    </Route>
                    <Route exact path={USER_PATH}>
                        <RequiresAuth loginPageRoute={LOGIN_PATH}>
                            {
                                !resources ? <LoadingPage /> :
                                    <UserPage getUser={ getUser }/>
                            }
                        </RequiresAuth>
                    </Route>
                    
                    {/* 404 */}
                    <Route exact path='/'>
                        <Redirect to={PROJECTS_PATH} />
                    </Route>
                    <Route component={NotFound} />
                </Switch>
            </Router>
        </UserContext.Provider>
    )
}

export { App } 