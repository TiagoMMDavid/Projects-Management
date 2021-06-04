import React, { useContext } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { UserContext } from '../utils/userSession'

function Navbar(): JSX.Element {
    const ctx = useContext(UserContext)
    const loc = useLocation()

    function logOutHandler() {
        ctx.logOut()
    }
    return (
        <UserContext.Consumer>
            {ctx => ctx?.credentials ? 
                <div className="navbar">
                    <div className="navbarContent left">
                        <span>{
                            loc.pathname == '/projects' ? 
                                <Link className="active" to="/projects">Projects</Link>
                                : 
                                <Link to="/projects">Projects</Link>
                        }</span>
                        <span>{
                            loc.pathname == '/users' ? 
                                <Link className="active" to="/users">Users</Link>
                                : 
                                <Link to="/users">Users</Link>
                        }</span>
                    </div>

                    <div className="navbarContent right">
                        <span>{
                            loc.pathname == `/users/${ctx.credentials.userId}` ? 
                                <Link className="active" to={`/users/${ctx.credentials.userId}`}>{ctx.credentials.username}</Link>
                                : 
                                <Link to={`/users/${ctx.credentials.userId}`}>{ctx.credentials.username}</Link>
                        }</span>

                        <span><a href="#" className="danger" onClick={logOutHandler}>Log-out</a></span>
                    </div>
                </div>
                :
                <></>
            }
        </UserContext.Consumer>
    )
}


export {
    Navbar
}