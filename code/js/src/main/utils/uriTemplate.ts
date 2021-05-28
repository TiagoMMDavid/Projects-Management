String.prototype.expandUriTemplate = function(this: string, name: string, value: string): string {
    return this.replace(`{${name}}`, value)
}

String.prototype.expandUriTemplate = function(this: string, ...values: string[]): string {
    const bracesRegex = /\{.*?\}/g
    const matchArray = []
    let match
    while (match = bracesRegex.exec(this)) {
        matchArray.push(match[0])
    }

    let toReturn = this
    matchArray.forEach((match, idx) => {
        toReturn = toReturn.replace(match, values[idx])
    })
    return toReturn
}