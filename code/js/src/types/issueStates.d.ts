type IssueStates = {
    states: IssueState[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type IssueState = {
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