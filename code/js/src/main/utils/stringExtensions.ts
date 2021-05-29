function registerExtensions(): void {
    String.prototype.expandUriTemplate = function(this: string, name: string, value: any): string {
        return this.replace(`{${name}}`, value)
    }
    
    String.prototype.expandUriTemplate = function(this: string, ...values: any[]): string {
        const bracesRegex = /\{.*?\}/g
        const matchArray = []
        let match = bracesRegex.exec(this)
        while (match) {
            matchArray.push(match[0])
            match = bracesRegex.exec(this)
        }
    
        let toReturn = `${this}`
        matchArray.forEach((match, idx) => {
            toReturn = toReturn.replace(match, values[idx])
        })
        return toReturn
    }
    
    String.prototype.toPaginatedUri = function(this: string, page: number, limit: number): string {
        return `${this}?page=${page}&limit=${limit}`
    }
}

export {
    registerExtensions
}