import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import { Paginated } from '../Paginated'
import queryString from 'query-string'
import { ProjectItem } from './ProjectItem'
import { CreateProject } from './CreateProject'
import { getProjects } from '../../api/projects'
import { getSirenAction } from '../../api/apiRoutes'

type State = {
    state: 'has-projects' | 'page-set' | 'message'
    projects: Projects
    message: string
}
  
type Action =
    { type: 'set-message', message: string } |
    { type: 'set-projects', projects: Projects, message: string } |
    { type: 'set-page', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-message': return { state: 'message', message: action.message } as State
        case 'set-projects': return { state: 'has-projects', projects: action.projects, message: action.message } as State
        case 'set-page': return { state: 'page-set', projects: null, message: action.message } as State
    }
}

function ProjectsPage(): JSX.Element {

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, projects, message }, dispatch] = useReducer(reducer, { state: 'page-set', projects: null } as State)
    const ctx = useContext(UserContext)

    useEffect(() => {
        let isCancelled = false
        if (state == 'page-set') {
            getProjects(page, ctx.credentials)
                .then(projects => {
                    if (isCancelled) return
                    dispatch({ type: 'set-projects', projects: projects, message: message })
                })
                .catch(err => {
                    if (isCancelled) return
                    dispatch({ type: 'set-message', message: err.message })
                })
        }

        return () => {
            isCancelled = true
        }
    }, [state])

    let projectsView: JSX.Element
    switch(state) {
        case 'message':
            projectsView = <h1>{message}</h1>
            break
        case 'page-set':
            projectsView = <h1>Loading projects...</h1>
            break
        case 'has-projects':
            projectsView = 
                <div>
                    <h4>{message}</h4>
                    { getSirenAction(projects.actions, 'create-project') != null ?
                        <CreateProject 
                            onFinishCreating={(success, message) => dispatch({ type: 'set-page', message: message, page: page } as Action)}
                            credentials={ctx.credentials} 
                        />
                        : <> </>
                    }
                    <hr/>
                    <Paginated 
                        onChangePage={() => dispatch({ type: 'set-page', message: message })} 
                        isLastPage={projects.isLastPage} page={projects.page}>
                        <h1>Projects</h1>
                        { projects.projects.length == 0 ? 
                            <p> No projects found </p> :
                            <ul>
                                {projects.projects.map((project: Project) => <ProjectItem key={project.id} project={project} />)}
                            </ul>
                        }
                    </Paginated>
                </div>
            break
    }

    return (
        <div>
            { projectsView }
        </div>
    )
}

export {
    ProjectsPage
}