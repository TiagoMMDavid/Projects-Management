import React from 'react'
import ReactDOM from 'react-dom'
import { App } from './main/App'
import { registerExtensions } from './main/utils/stringExtensions'

registerExtensions()

ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('container')
)