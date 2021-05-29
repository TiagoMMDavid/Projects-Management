import React from 'react'

type ProjectsGetter = (page: number, limit: number) => Projects

type ProjectsPageProps = {
    getProjects: ProjectsGetter
}

function ProjectsPage(): JSX.Element {
    return (
        <h1>Projects</h1>
    )
}

export {
    ProjectsPage
}