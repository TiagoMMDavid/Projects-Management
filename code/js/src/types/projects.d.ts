type Projects = {
    projects: Project[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type Project = {
    id: number,
    name: string
    description: string,
    author: string
    authorId: number

    links: SirenLink[],
    actions: SirenAction[]
}