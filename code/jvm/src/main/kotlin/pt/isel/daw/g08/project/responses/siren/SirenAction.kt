package pt.isel.daw.g08.project.responses.siren

import com.fasterxml.jackson.annotation.JsonInclude

class SirenAction(
    val name: String,
    val title: String,
    val method: String,
    val href: String,
    val type: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var fields: MutableList<LinkedHashMap<String, String>>? = null
) {
    fun addField(name: String, type: SirenFieldType, value: String? = null) {
        if (fields == null) fields = mutableListOf()

        val toAdd = LinkedHashMap<String, String>()

        toAdd["name"] = name
        toAdd["type"] = type.toString()
        if (value != null) {
            toAdd["value"] = value
        }

        fields!!.add(toAdd)
    }
}