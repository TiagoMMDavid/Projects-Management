package pt.isel.daw.g08.project.responses.siren

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpMethod

@JsonInclude(JsonInclude.Include.NON_NULL)
class SirenAction(
    val name: String,
    val title: String,
    val method: HttpMethod,
    val href: String,
    val type: String? = null,
    var fields: MutableList<LinkedHashMap<String, Any>>? = null
) {
    fun addField(name: String, type: SirenFieldType, value: Any? = null): SirenAction {
        if (fields == null) fields = mutableListOf()

        val toAdd = LinkedHashMap<String, Any>()

        toAdd["name"] = name
        toAdd["type"] = type.toString()
        if (value != null) {
            toAdd["value"] = value
        }

        fields!!.add(toAdd)
        return this
    }
}