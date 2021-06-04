import React, { useContext, useEffect, useReducer, } from 'react'
import { useParams } from 'react-router'
import { Link, Redirect } from 'react-router-dom'
import { getProject } from '../../api/projects'
import { UserContext } from '../../utils/userSession'
import { DeleteProject } from './DeleteProject'
import { EditProject } from './EditProject'

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
            <p><Link to={`/projects/${project.id}/labels`}>View Labels</Link></p>
            <p><Link to={`/projects/${project.id}/states`}>View States</Link></p>
            <p><Link to={`/projects/${project.id}/issues`}>View Issues</Link></p>
        </div>
    )
}

type State = {
    state: 'has-project' | 'loading-project' | 'deleted-project' | 'edited-project' | 'project-fail' | 'message'
    message: string
    project: Project
}
  
type Action =
    { type: 'set-project', project: Project, message: string } |
    { type: 'loading-project', message: string } |
    { type: 'set-deleted-project' } |
    { type: 'set-edited-project' } |
    { type: 'set-message', message: string }
    
function reducer(state: State, action: Action): State {
    switch (action.type) {
        case 'set-project': return { state: 'has-project', project: action.project, message: action.message } as State
        case 'loading-project': return { state: 'loading-project', message: action.message } as State
        case 'set-deleted-project': return { state: 'deleted-project' } as State
        case 'set-edited-project': return { state: 'edited-project' } as State
        case 'set-message': return { state: 'message', message: action.message } as State
    }
}

function ProjectPage(): JSX.Element {
    const { projectId } = useParams<ProjectPageParams>()
    const [{ state, project, message }, dispatch] = useReducer(reducer, {state: 'loading-project'} as State) 
    const ctx = useContext(UserContext)

    useEffect(() => {
        if (state == 'edited-project' || state == 'loading-project' || state == 'project-fail') {
            getProject(Number(projectId), ctx.credentials)
                .then(project => dispatch({ type: 'set-project', project: project, message: message }))
                .catch(err => dispatch({ type: 'set-message', message: err.message }))
        }
    }, [projectId, state])

    if (state == 'deleted-project')
        return (
            <Redirect to="/projects" />
        )

    let body: JSX.Element
    switch(state) {
        case 'message':
            body = (
                <h1>{message}</h1>
            )
            break
        case 'edited-project':
        case 'loading-project': 
            body = (
                <h1>Loading project...</h1> 
            )
            break
        case 'has-project':
            body = (
                <div>
                    <h4>{message}</h4>
                    <EditProject
                        project={project}
                        onFinishEdit={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-edited-project' })
                            } else {
                                dispatch({ type: 'loading-project', message: message })
                            }
                        }}
                        onEdit={() => dispatch({ type: 'set-message', message: 'Editing Project...' })}
                        credentials={ctx.credentials} 
                    />
                    <DeleteProject
                        project={project}
                        onFinishDelete={(success, message) => {
                            if (success) {
                                dispatch({ type: 'set-deleted-project' })
                            } else {
                                dispatch({ type: 'loading-project', message: message })
                            }
                        }}
                        onDelete={() => dispatch({ type: 'set-message', message: 'Deleting Project...' })}
                        credentials={ctx.credentials}
                    />
                </div>
            )
            break
    }
    return (
        <div>
            <Link to="/projects">{'<< View all projects'}</Link>
            {body}
            {project == null ? <></> :  <Project project={project}/>}
        </div>
    )
}

export {
    ProjectPage
}