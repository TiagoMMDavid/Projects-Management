package pt.isel.daw.g08.project.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter

data class Link(
    val href: String,
)

abstract class Hal : Response {

    override fun getContentType() = "application/hal+json"
    private val properties = LinkedHashMap<String, Any>()

    constructor(self: String) {
        properties["_links"] = LinkedHashMap<String, Link>()
        addLink("self", self)
    }

    fun addLink(name: String, href: String): Hal {
        val linksMap = properties["_links"] as LinkedHashMap<String, Link>
        linksMap[name] = Link(href)
        return this
    }

    fun addEmbedded(name: String, value: Any): Hal {
        val embedded: LinkedHashMap<String, Any>
        if (!properties.containsKey("_embedded")) {
            embedded = LinkedHashMap()
            properties["_embedded"] = embedded
        } else {
            embedded = properties["_embedded"] as LinkedHashMap<String, Any>
        }

        embedded[name] = value
        return this
    }

    fun addProperty(name: String, value: Any): Hal {
        properties[name] = value
        return this
    }

    @JsonAnyGetter
    fun getProperties() = properties
}