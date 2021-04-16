package pt.isel.daw.g08.project.responses.siren

import com.fasterxml.jackson.annotation.JsonAnyGetter
import pt.isel.daw.g08.project.responses.Response

abstract class Siren : Response {

    override fun getContentType() = "application/vnd.siren+json"
    private val jsonProperties = LinkedHashMap<String, Any>()
    private var isFinal = false

    constructor(self: String, vararg classes: SirenClass) {
        jsonProperties["class"] = classes
        jsonProperties["rel"] = mutableListOf<String>()
        jsonProperties["properties"] = LinkedHashMap<String, Any?>()
        jsonProperties["entities"] = mutableListOf<HashMap<String, Any?>>()
        jsonProperties["actions"] = mutableListOf<SirenAction>()
        jsonProperties["links"] = mutableListOf<HashMap<String, Any?>>()
        addLink(self, false, "self")
    }

    fun addRelation(rel: String): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val relationsList = jsonProperties["rel"] as MutableList<String>
        relationsList.add(rel)

        return this
    }

    fun addProperty(name: String, value: Any?): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val propertiesMap = jsonProperties["properties"] as LinkedHashMap<String, Any?>
        propertiesMap[name] = value
        return this
    }

    fun addEntity(entity: HashMap<String, Any?>): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val entitiesList = jsonProperties["entities"] as MutableList<HashMap<String, Any?>>
        entitiesList.add(entity)

        return this
    }

    fun addAction(action: SirenAction): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val actionList = jsonProperties["actions"] as MutableList<SirenAction>
        actionList.add(action)

        return this
    }

    fun addLink(href: String, isTemplate: Boolean, vararg rels: String): Siren {
        if (isFinal) throw IllegalStateException("Cannot add elements after getting JSON properties")

        val linksList = jsonProperties["links"] as MutableList<HashMap<String, Any?>>
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
    fun getJsonProperties(): HashMap<String, Any?> {
        isFinal = true

        if ((jsonProperties["rel"] as List<String>).isEmpty()) jsonProperties.remove("rel")
        if ((jsonProperties["properties"] as LinkedHashMap<String, Any?>).isEmpty()) jsonProperties.remove("properties")
        if ((jsonProperties["entities"] as List<HashMap<String, Any?>>).isEmpty()) jsonProperties.remove("entities")
        if ((jsonProperties["actions"] as List<SirenAction>).isEmpty()) jsonProperties.remove("actions")

        return jsonProperties
    }
}