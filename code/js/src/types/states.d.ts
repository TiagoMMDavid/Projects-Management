type States = {
    states: State[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type State = {
    id: number,
    number: number,
    name: string,
    isStartState: boolean,
    project: string,
    projectId: number,
    author: string,
    authorId: number,

    links: SirenLink[],
    actions: SirenAction[]
}