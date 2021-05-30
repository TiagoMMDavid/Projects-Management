import React from 'react'
import { Link } from 'react-router-dom'

type UserProps = {
    user: User
}

function UserItem({ user }: UserProps): JSX.Element {
    return (
        <ul>
            <li>
                <p>Id: <Link to={`/users/${user.id}`}>{user.id}</Link></p>
                <p>Name: {user.name}</p>
            </li>
        </ul>
    )
}

export {
    UserItem
}