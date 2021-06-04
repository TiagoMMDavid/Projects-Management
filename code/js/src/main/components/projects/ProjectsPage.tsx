import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation } from 'react-router-dom'
import { UserContext } from '../../utils/userSession'
import { Paginated } from '../Paginated'
import queryString from 'query-string'
import { ProjectItem } from './ProjectItem'
import { CreateProject } from './CreateProject'
import { getProjects } from '../../api/projects'

type State = {
    state: 'has-projects' | 'loading-projects' | 'page-reset' | 'hide' | 'message'
    projects: Projects
    message: string
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-message', message: string } |
    { type: 'set-projects', projects: Projects } |
    { type: 'reset-page' } |
    { type: 'hide' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-message': return { state: 'message', message: action.message } as State
        case 'set-loading': return { state: 'loading-projects', projects: null } as State
        case 'set-projects': return { state: 'has-projects', projects: action.projects } as State
        case 'reset-page': return { state: 'page-reset', projects: null } as State
        case 'hide': return { state: 'hide', projects: null } as State
    }
}

function ProjectsPage(): JSX.Element {

    const pageQuery = Number(queryString.parse(useLocation().search).page) || 0
    const page = pageQuery < 0 ? 0 : pageQuery

    const [{ state, projects, message }, dispatch] = useReducer(reducer, { state: 'page-reset', projects: null } as State)
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getProjects(page, ctx.credentials)
            .then(projects => dispatch({ type: 'set-projects', projects: projects }))
            .catch(err => dispatch({ type: 'set-message', message: err.message }))
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let projectsView: JSX.Element
    switch(state) {
        case 'message':
            projectsView = <h1>{message}</h1>
            break
        case 'hide':
            break
        case 'page-reset':
        case 'loading-projects':
            projectsView = <h1>Loading projects...</h1>
            break
        case 'has-projects':
            projectsView = 
                <div>
                    <Paginated onChangePage={getPage} isLastPage={projects.isLastPage} page={projects.page}>
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
            <CreateProject 
                onFinishCreating={() => dispatch({type: 'reset-page'})} 
                onCreating={() => dispatch({type: 'hide'})}
                credentials={ctx.credentials} 
            />
            { projectsView }
        </div>
    )
}

export {
    ProjectsPage
}