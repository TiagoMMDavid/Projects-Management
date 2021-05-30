type Labels = {
    labels: Label[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type Label = {
    id: number,
    number: number,
    name: string,
    project: string,
    projectId: number,
    author: string,
    authorId: number

    links: SirenLink[],
    actions: SirenAction[]
}