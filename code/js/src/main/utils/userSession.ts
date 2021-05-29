import { createContext } from 'react'

const SESSION_STORAGE_KEY = 'UserCredentials'

type Credentials = {
    scheme: 'Basic',
    content: string,
    username: string
}

function logIn(username: string, password: string): Credentials {
    const credentials = generateCredentials(username, password)
    sessionStorage.setItem(SESSION_STORAGE_KEY, credentials.content)
    return credentials
}

function getStoredCredentials(): Credentials {
    let toReturn = null
    const storedCredentials = sessionStorage.getItem(SESSION_STORAGE_KEY)
    if (storedCredentials) {
        const decoded = atob(storedCredentials)
        const username = decoded.split(':')[0]
        toReturn = {
            scheme: 'Basic',
            content: storedCredentials,
            username: username
        } as Credentials
    }

    return toReturn
}

function generateCredentials(username: string, password: string): Credentials {
    return {
        scheme: 'Basic',
        content: btoa(`${username}:${password}`),
        username: username
    } as Credentials
}

function logOut(): void {
    sessionStorage.removeItem(SESSION_STORAGE_KEY)
}

function getInitialContext(state: Credentials, setState: (cred: Credentials) => void): UserContextType {
    return {
        credentials: state,
        logIn: (username: string, password: string) => {
            const credentials = logIn(username, password)
            setState(credentials)
        },
        logOut: () => {
            logOut()
            setState(null)
        }
    } as UserContextType
}

type UserContextType = {
    credentials?: Credentials,
    logIn: (username: string, password: string) => Credentials
    logOut: () => void
}

const UserContext = createContext<UserContextType | undefined>(undefined)

export {
    generateCredentials,
    getInitialContext,
    getStoredCredentials,
    UserContext,
    UserContextType,
    Credentials
}