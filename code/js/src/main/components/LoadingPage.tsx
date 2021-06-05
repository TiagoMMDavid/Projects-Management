import React from 'react'

type LoadingPageProps = {
    loadingMsg: string
    loadFailed: boolean
}

function LoadingPage({ loadingMsg, loadFailed }: LoadingPageProps): JSX.Element {
    if (loadFailed) {
        return (
            <div className="center">
                <p className="mt-5">{loadingMsg}</p>
            </div>
        )  
    }

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