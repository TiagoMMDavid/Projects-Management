package pt.isel.daw.g08.project.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter

abstract class Siren : Response {

    override fun getContentType() = "application/vnd.siren+json"
    private val jsonProperties = LinkedHashMap<String, Any>()
    private var isFinal = false

    constructor(self: String, vararg classes: SirenClass) {
        jsonProperties["class"] = classes
        jsonProperties["rel"] = mutableListOf<String>()
        jsonProperties["properties"] = LinkedHashMap<String, Any>()
        jsonProperties["entities"] = mutableListOf<HashMap<String, Any>>()
        jsonProperties["actions"] = mutableListOf<HashMap<String, Any>>()
        jsonProperties["links"] = mutableListOf<HashMap<String, Any>>()
        addLink(false, self, "self")
    }

    fun addRelation(rel: String): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val relationsList = jsonProperties["rel"] as MutableList<String>
        relationsList.add(rel)

        return this
    }

    fun addProperty(name: String, value: Any): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val propertiesMap = jsonProperties["properties"] as LinkedHashMap<String, Any>
        propertiesMap[name] = value
        return this
    }

    fun addEntity(entity: HashMap<String, Any>): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val entitiesList = jsonProperties["entities"] as MutableList<HashMap<String, Any>>
        entitiesList.add(entity)

        return this
    }

    fun addAction() {
        TODO()
    }

    fun addLink(isTemplate: Boolean, href: String, vararg rels: String) : Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val linksList = jsonProperties["links"] as MutableList<HashMap<String, Any>>
        val toAdd = LinkedHashMap<String, Any>()
        toAdd["rel"] = rels
        if (isTemplate) {
            toAdd["hrefTemplate"] = href
        } else {
            toAdd["href"] = href
        }
        linksList.add(toAdd)
        return this
    }


    // Final operation: once this method is executed, the resulting JSON will not have these fields
    @JsonAnyGetter
    fun getJsonProperties(): HashMap<String, Any> {
        isFinal = true

        if ((jsonProperties["rel"] as List<String>).isEmpty()) jsonProperties.remove("rel")
        if ((jsonProperties["properties"] as LinkedHashMap<String, Any>).isEmpty()) jsonProperties.remove("properties")
        if ((jsonProperties["entities"] as List<HashMap<String, Any>>).isEmpty()) jsonProperties.remove("entities")
        if ((jsonProperties["actions"] as List<HashMap<String, Any>>).isEmpty()) jsonProperties.remove("actions")

        return jsonProperties
    }
}