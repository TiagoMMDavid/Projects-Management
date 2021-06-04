import React from 'react'

type LoadingPageProps = {
    loadingMsg: string
}

function LoadingPage({ loadingMsg }: LoadingPageProps): JSX.Element {
    return (
        <div className="center">
            <div className="spinner">
                <div className="cube1"></div>
                <div className="cube2"></div>
            </div>
            <p className="mt-5">{loadingMsg}</p>
        </div>
    )
}

export {
    LoadingPage
}