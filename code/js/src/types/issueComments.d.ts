type IssueComments = {
    comments: IssueComment[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type IssueComment = {
    id: number,
    number: number,
    content: string,
    createDate: string,
    issue: string,
    issueNumber: number,
    project: string,
    projectId: number,
    author: string,
    authorId: number,

    links: SirenLink[],
    actions: SirenAction[]
}