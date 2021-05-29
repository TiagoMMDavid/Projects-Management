import React, { useContext } from 'react'
import { UserContext } from '../utils/userSession'

function Header(): JSX.Element {
    const ctx = useContext(UserContext)
    
    function logOutHandler() {
        ctx.logOut()
    }

    return (
        <UserContext.Consumer>
            {ctx => ctx?.credentials ? 
                <div>
                    <span>{ctx.credentials.username} </span>
                    <a href="#" onClick={logOutHandler}>Log-out</a>
                    <hr></hr>
                </div>
                : 
                <></>
            }
        </UserContext.Consumer>
    )
}


export {
    Header
}