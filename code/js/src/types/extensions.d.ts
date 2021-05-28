interface String {
    expandUriTemplate(name: string, value: string): string,
    expandUriTemplate(...values: string[]): string,
}
