import React, { useContext, useEffect, useReducer } from 'react'
import { useLocation } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import { Paginated } from '../Paginated'
import queryString from 'query-string'
import { ProjectItem } from './ProjectItem'
import { CreateProject } from './CreateProject'

type ProjectsGetter = (page: number, credentials: Credentials) => Promise<Projects>

type ProjectsPageProps = {
    getProjects: ProjectsGetter
}

type State = {
    state: 'has-projects' | 'loading-projects' | 'page-reset' | 'hide'
    projects: Projects
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-projects', projects: Projects } |
    { type: 'reset-page' } |
    { type: 'hide' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-projects', projects: null}
        case 'set-projects': return { state: 'has-projects', projects: action.projects }
        case 'reset-page': return { state: 'page-reset', projects: null }
        case 'hide': return { state: 'hide', projects: null }
    }
}

function ProjectsPage({ getProjects }: ProjectsPageProps): JSX.Element {

    const page = Number(queryString.parse(useLocation().search).page) || 0

    const [{ state, projects }, dispatch] = useReducer(reducer, { state: 'page-reset', projects: null })
    const ctx = useContext(UserContext)

    function getPage(page: number): void {
        dispatch({type: 'set-loading'})
        getProjects(page, ctx.credentials)
            .then(projects => {
                // TODO: Error handling
                dispatch({ type: 'set-projects', projects: projects})
            })
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    let projectsView: JSX.Element
    switch(state) {
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