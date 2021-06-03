import React, { ReactNode } from 'react'
import { useHistory } from 'react-router'

type PaginatedProps = {
    children?: ReactNode,
    onChangePage: (page: number) => void,
    page: number,
    isLastPage: boolean
}

function Paginated({ children, onChangePage, page, isLastPage }: PaginatedProps): JSX.Element {
    const history = useHistory()

    function handlePageChange(page: number) {
        const params = new URLSearchParams()

        params.append('page', `${page}`)
        history.push({search: params.toString()})

        onChangePage(page)
    }

    return (
        <div>
            <> {children} </>
            {page != 0 ? <button onClick={() => handlePageChange(page - 1)}>{'<<'}</button> : <></> }
            {!isLastPage ? <button onClick={() => handlePageChange(page + 1)}>{'>>'}</button> : <></> }
            {page != 0 || !isLastPage ? <span> (page {page})</span> : <></>}
        </div>
    )
}

export {
    Paginated
}