interface String {
    expandUriTemplate(name: string, value: any): string,
    expandUriTemplate(...values: any[]): string,
    toPaginatedUri(page: number, limit: number): string
}
