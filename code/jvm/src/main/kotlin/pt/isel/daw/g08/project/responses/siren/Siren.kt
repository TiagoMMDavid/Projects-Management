package pt.isel.daw.g08.project.responses.siren

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.util.UriTemplate
import pt.isel.daw.g08.project.responses.Response
import java.net.URI

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenAction(
    val name: String,
    val title: String,
    val method: HttpMethod,
    val href: URI? = null,
    @JsonSerialize(using = ToStringSerializer::class)
    val hrefTemplate: UriTemplate? = null,
    @JsonSerialize(using = ToStringSerializer::class)
    val type: MediaType? = null,
    var fields: List<SirenActionField>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenActionField(
    val name: String,
    val title: String? = null,
    val type: SirenFieldType? = null,
    val value: Any? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenLink(
    val rel: List<String>,
    val href: URI? = null,
    val hrefTemplate: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class Siren<T>(
    @JsonProperty("class") val clazz: List<SirenClass>,
    val rel: List<String>? = null,
    val properties: T? = null,
    val entities: List<Any>? = null,
    val actions: List<SirenAction>? = null,
    val links: List<SirenLink>, // Can't be null because 'self' link is always required
) : Response {

    override fun getContentType() = "application/vnd.siren+json"
}