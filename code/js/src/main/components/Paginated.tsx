import React, { ReactNode } from 'react'

type PaginatedProps = {
    children?: ReactNode,
    onChangePage: (page: number) => void,
    page: number,
    isLastPage: boolean
}

function Paginated({ children, onChangePage, page, isLastPage }: PaginatedProps): JSX.Element {
    return (
        <div>
            <> {children} </>
            {page != 0 ? <button onClick={() => onChangePage(page - 1)}>{'<<'}</button> : <></> }
            {!isLastPage ? <button onClick={() => onChangePage(page + 1)}>{'>>'}</button> : <></> }
            {page != 0 || !isLastPage ? <span> (page {page})</span> : <></>}
        </div>
    )
}

export {
    Paginated
}