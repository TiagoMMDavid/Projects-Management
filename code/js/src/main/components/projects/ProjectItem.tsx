import React from 'react'
import { Link } from 'react-router-dom'

type ProjectProps = {
    project: Project
}

function ProjectItem({ project }: ProjectProps): JSX.Element {
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

export {
    ProjectItem
}