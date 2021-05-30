type Issues = {
    issues: Issue[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type Issue = {
    id: number,
    number: number,
    name: string,
    description: string,
    createDate: string,
    closeDate?: string,
    state: string,
    stateNumber: number,
    project: string,
    projectId: number,
    author: string,
    authorId: number,

    links: SirenLink[],
    actions: SirenAction[]
}