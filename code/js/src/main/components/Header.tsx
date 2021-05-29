import React, { useContext } from 'react'
import { Link } from 'react-router-dom'
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
                    <span><Link to="/projects">Projects</Link> </span>
                    <span><a href="#" onClick={logOutHandler}>Log-out</a> </span>
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