import { createContext } from 'react'

const SESSION_STORAGE_KEY = 'UserCredentials'

type Credentials = {
    scheme: 'Basic',
    content: string,
    username: string,
    userId: number
}

function generateCredentials(username: string, password: string, userId: number = null): Credentials {
    return {
        scheme: 'Basic',
        content: btoa(`${username}:${password}`),
        username: username,
        userId: userId
    } as Credentials
}

function getStoredCredentials(): Credentials {
    let toReturn = null
    const storedCredentials = localStorage.getItem(SESSION_STORAGE_KEY)
    if (storedCredentials) {
        toReturn = JSON.parse(storedCredentials)
    }

    return toReturn
}

function logIn(username: string, password: string, userId: number): Credentials {
    const credentials = generateCredentials(username, password, userId)
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(credentials))
    return credentials
}

function logOut(): void {
    localStorage.removeItem(SESSION_STORAGE_KEY)
}


type UserContextType = {
    credentials?: Credentials,
    logIn: (username: string, password: string, userId: number) => Credentials
    logOut: () => void
}

const UserContext = createContext<UserContextType | undefined>(undefined)

function getInitialContext(state: Credentials, setState: (cred: Credentials) => void): UserContextType {
    return {
        credentials: state,
        logIn: (username: string, password: string, userId: number) => {
            const credentials = logIn(username, password, userId)
            setState(credentials)
        },
        logOut: () => {
            logOut()
            setState(null)
        }
    } as UserContextType
}

export {
    generateCredentials,
    getInitialContext,
    getStoredCredentials,
    UserContext,
    UserContextType,
    Credentials
}