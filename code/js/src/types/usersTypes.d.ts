type Users = {
    users: User[],
    page: number,
    isLastPage: boolean,

    links: SirenLink[]
    actions: SirenAction[]
}

type User = {
    id: number,
    name: string

    links: SirenLink[]
    actions: SirenAction[]
}