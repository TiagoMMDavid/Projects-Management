import React, { useContext, useEffect, useReducer } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { Credentials, UserContext } from '../../utils/userSession'
import { DeleteProject } from './DeleteProject'
import { EditProject } from './EditProject'

type ProjectPageProps = {
    getProject: (projectId: number, credentials: Credentials) => Promise<Project>
}
type ProjectProps = {
    project: Project
}

type ProjectPageParams = {
    projectId: string
}

function Project({ project }: ProjectProps): JSX.Element {
    return (
        <div>
            <p>Id: {project.id}</p>
            <p>Name: {project.name}</p>
            <p>Description: {project.description}</p>
            <p>Author: <Link to={`/users/${project.authorId}`}>{project.author}</Link></p>
            <p><Link to={`/projects/${project.id}/labels`}>View labels</Link></p>
            <p><Link to={`/projects/${project.id}/states`}>View States</Link></p>
            <p><Link to={`/projects/${project.id}/issues`}>View Issues</Link></p>
        </div>
    )
}

type State = {
    state: 'has-project' | 'loading-project' | 'no-project' | 'deleted-project' | 'edited-project'
    project: Project
}
  
type Action =
    { type: 'set-loading' } |
    { type: 'set-project', project: Project} |
    { type: 'set-no-project' } |
    { type: 'set-deleted-project' } |
    { type: 'set-edited-project' }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-loading': return { state: 'loading-project', project: null}
        case 'set-project': return { state: 'has-project', project: action.project}
        case 'set-no-project': return { state: 'no-project', project: null}
        case 'set-deleted-project': return { state: 'deleted-project', project: null}
        case 'set-edited-project': return { state: 'edited-project', project: null}
    }
}

function ProjectPage({ getProject }: ProjectPageProps): JSX.Element {
    const { projectId } = useParams<ProjectPageParams>()
    const [{ state, project }, dispatch] = useReducer(reducer, {state: 'loading-project', project: null})
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-project' || state == 'loading-project') {
            getProject(Number(projectId), ctx.credentials)
                .then(project => {
                    if (project) dispatch({ type: 'set-project', project: project })
                    else dispatch({ type: 'set-no-project' })
                })
        }
    }, [projectId, state])

    if (state == 'deleted-project')
        return (
            <Redirect to="/projects" />
        )

    let toReturn: JSX.Element
    switch(state) {
        case 'edited-project':
        case 'loading-project': 
            toReturn = (
                <h1> Loading project... </h1> 
            )
            break
        case 'no-project':
            toReturn = (
                <h1> Project not found </h1> 
            )
            break
        case 'has-project':
            toReturn = (
                <div>
                    <EditProject
                        project={project}
                        onFinishEdit={() => dispatch({ type: 'set-edited-project' })}
                        credentials={ctx.credentials} 
                    />
                    <DeleteProject
                        project={project}
                        onFinishDelete={() => dispatch({ type: 'set-deleted-project' })}
                        credentials={ctx.credentials}
                    />
                    <Project project={project}/>
                </div>
            )
            break
    }
    return (
        <div>
            <Link to="/projects">View all projects</Link>
            {toReturn}
        </div>
    )
}

export {
    ProjectPage
}