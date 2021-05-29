import React, { useContext, useEffect, useReducer, useRef, useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Credentials, UserContext } from '../utils/userSession'
import { Paginated } from './Paginated'
import queryString from 'query-string'
import { createProject } from '../api/projects'

type ProjectsGetter = (page: number, credentials: Credentials) => Promise<Projects>

type ProjectsPageProps = {
    getProjects: ProjectsGetter
}

type ProjectProps = {
    project: Project
}

function Project({ project }: ProjectProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Name: <Link to={`/projects/${project.id}`}>{project.name}</Link></p>
                <p>Description: {project.description}</p>
                <p>Author: <Link to={`/users/${project.authorId}`}>{project.author}</Link></p>
            </li>
        </ul>
    )
}

type State = {
    state: 'has-projects' | 'loading-projects' | 'creating-project' | 'page-reset'
    projects: Projects
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-projects', projects: Projects } |
    { type: 'set-creating' } |
    { type: 'reset-page' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-projects', projects: null}
        case 'set-projects': return { state: 'has-projects', projects: action.projects }
        case 'set-creating': return { state: 'creating-project', projects: null }
        case 'reset-page': return { state: 'page-reset', projects: null }
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
                dispatch({ type: 'set-projects', projects: projects})
            })
    }
    
    function CreateProject(): JSX.Element {
        const [error, setError] = useState(null)
        const name = useRef<HTMLInputElement>(null)
        const description = useRef<HTMLInputElement>(null)
    
        function createProjectHandler() {
            const nameInput = name.current?.value
            const descriptionInput = description.current?.value
            if (nameInput.length == 0) {
                return setError('Name can\'t be empty!')
            }
            if (descriptionInput.length == 0) {
                return setError('Description can\'t be empty!')
            }
            dispatch({type: 'set-creating'})
            createProject(nameInput, descriptionInput, ctx.credentials)
                .then(res => {
                    if (res) dispatch({type: 'reset-page'})
                    else setError('Failed to create project') 
                })
        }
    
        return (
            <div>
                <h2>Create Project</h2>
                <input type="text" placeholder="Project Name" ref={name} onChange={() => setError(null)}/>
                <input type="text" placeholder="Project Description" ref={description} />
                <button onClick={createProjectHandler}>Create</button>
                <p>{error}</p>
            </div>
        )
    }

    useEffect(() => {
        if (state == 'page-reset') {
            getPage(page)
        }
    }, [state])

    return (
        state == 'creating-project' 
            ? 
            <h1>Creating project...</h1> 
            :
            <div>
                <CreateProject />
                {
                    !projects 
                        ? 
                        <h1>Loading projects...</h1>
                        :
                        <div>
                            <Paginated onChangePage={getPage} isLastPage={projects.isLastPage} page={projects.page}>
                                <h1>Projects</h1>
                                { projects.projects.length == 0 ? <p> No projects found </p> :
                                    projects.projects.map((project: Project) => <Project key={project.id} project={project} />)}
                            </Paginated>
                        </div>
                }
            </div>
    )
}

export {
    ProjectsPage
}