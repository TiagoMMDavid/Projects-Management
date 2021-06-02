import { Credentials } from '../utils/userSession'

const API_HOST = 'localhost:8080'

type NavigationLink = {
    href?: string,
    hrefTemplate?: string 
}

type ApiRoutes = {
    home: HomeRoutes,
    project: ProjectRoutes,
    label: LabelRoutes,
    state: StateRoutes,
    issue: IssueRoutes,
    comment: CommentRoutes,
    user: UserRoutes
}

export type HomeRoutes = {
    getHomeRoute: NavigationLink
}

type ProjectRoutes = {
    getProjectsRoute: NavigationLink
    getProjectRoute: NavigationLink
}

type LabelRoutes = {
    getLabelsRoute: NavigationLink,
    getLabelRoute: NavigationLink
} 

type StateRoutes = {
    getStatesRoute: NavigationLink,
    getStateRoute: NavigationLink,
    getNextStatesRoute: NavigationLink
}

type IssueRoutes = {
    getIssuesRoute: NavigationLink,
    getIssueRoute: NavigationLink,
    getIssueLabelsRoute: NavigationLink
}

type CommentRoutes = {
    getCommentsRoute: NavigationLink,
    getCommentRoute: NavigationLink
}

type UserRoutes = {
    getUsersRoute: NavigationLink,
    getUserRoute: NavigationLink,
    getAuthenticatedUserRoute: NavigationLink
}

const apiRoutes = {
    home: {
        getHomeRoute: { href: '/api'} as NavigationLink
    } as HomeRoutes
} as ApiRoutes


function getRequestOptions(method: string, credentials: Credentials, body: any = null): RequestInit {
    return {
        method: method,
        headers: {
            'Host': API_HOST,
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `${credentials?.scheme} ${credentials?.content}`
        },
        body: body ? JSON.stringify(body) : null
    }
}

function getSirenLink(links: SirenLink[], rel: string): SirenLink {
    return links
        .find(link => {
            const relArr = Array.from(link.rel)
            return relArr.includes(rel)
        })
}

function getSirenAction(actions: SirenAction[], name: string): SirenAction {
    return actions?.find(action => action.name == name)
}

function fetchRoutes(): Promise<ApiRoutes> {
    return fetch(apiRoutes.home.getHomeRoute.href, getRequestOptions('GET', null))
        .then(res => res.status == 200 ? res.json() : null)
        .then(entity => {
            if (!entity) return null

            const links: SirenLink[] = Array.from(entity.links)

            apiRoutes.project = {
                getProjectsRoute: {
                    href: getSirenLink(links, 'projects').href
                },
                getProjectRoute: {
                    hrefTemplate: getSirenLink(links, 'project').hrefTemplate
                }
            }

            apiRoutes.label = {
                getLabelsRoute: {
                    hrefTemplate: getSirenLink(links, 'labels').hrefTemplate
                },
                getLabelRoute: {
                    hrefTemplate: getSirenLink(links, 'label').hrefTemplate 
                }
            }

            apiRoutes.state = {
                getStatesRoute: {
                    hrefTemplate: getSirenLink(links, 'states').hrefTemplate
                },
                getStateRoute: {
                    hrefTemplate: getSirenLink(links, 'state').hrefTemplate
                },
                getNextStatesRoute: {
                    hrefTemplate: getSirenLink(links, 'nextStates').hrefTemplate
                }
            }

            apiRoutes.issue = {
                getIssuesRoute: {
                    hrefTemplate: getSirenLink(links, 'issues').hrefTemplate
                },
                getIssueRoute: {
                    hrefTemplate: getSirenLink(links, 'issue').hrefTemplate
                },
                getIssueLabelsRoute: {
                    hrefTemplate: getSirenLink(links, 'issueLabels').hrefTemplate
                },

            }

            apiRoutes.comment = {
                getCommentsRoute: {
                    hrefTemplate: getSirenLink(links, 'comments').hrefTemplate
                },
                getCommentRoute: {
                    hrefTemplate: getSirenLink(links, 'comment').hrefTemplate
                }
            }

            apiRoutes.user = {
                getUsersRoute: {
                    href: getSirenLink(links, 'users').href
                },
                getUserRoute: {
                    hrefTemplate: getSirenLink(links, 'user').hrefTemplate
                },
                getAuthenticatedUserRoute: {
                    href: getSirenLink(links, 'authUser').href
                },
            }

            return apiRoutes
        })
}

export {
    fetchRoutes,
    apiRoutes,
    ApiRoutes,
    getRequestOptions,
    getSirenAction
}

