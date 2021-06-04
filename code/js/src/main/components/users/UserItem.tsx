import React from 'react'
import { Link } from 'react-router-dom'

type UserProps = {
    user: User
}

function UserItem({ user }: UserProps): JSX.Element {
    return (
        <li>
            <p>Name: <Link to={`/users/${user.id}`}>{user.name}</Link></p>
        </li>
    )
}

export {
    UserItem
}