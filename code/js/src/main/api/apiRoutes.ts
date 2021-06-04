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
    getLabelRoute: NavigationLink,
    getIssueLabelsRoute: NavigationLink
} 

type StateRoutes = {
    getStatesRoute: NavigationLink,
    getStateRoute: NavigationLink,
    getNextStatesRoute: NavigationLink
}

type IssueRoutes = {
    getIssuesRoute: NavigationLink,
    getIssueRoute: NavigationLink,
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

async function throwErrorFromResponse(res: Response, genericMessage: string): Promise<void> {
    let message: string
    try {
        const json = await res.json()

        switch (json.type) {
            case '/problems/resource-not-found':
                message = 'Page does not exist'
                break
            case '/problems/resource-already-exists':
                message = 'Item already exists'
                break
            case '/problems/resource-referenced':
                message = 'Cannot modify an item that is referenced by other items'
                break
            case '/problems/invalid-string-size':
                message = 'Input too long'
                break
            case '/problems/invalid-input':
            case '/problems/forbidden-operation':
            case '/problems/no-start-state':
            case '/problems/invalid-state-transition':
            case '/problems/archived-issue':
            case '/problems/forbidden-state-modification':
                message = json.detail
                break
            default:
                message = genericMessage
        }
    } catch {
        throw {
            status: res.status,
            message: genericMessage
        } as ApiError
    }
    
    throw {
        status: res.status,
        message: message
    } as ApiError
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
                },
                getIssueLabelsRoute: {
                    hrefTemplate: getSirenLink(links, 'issueLabels').hrefTemplate
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
                }
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
    getSirenAction,
    throwErrorFromResponse
}

