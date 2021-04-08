package pt.isel.daw.g08.project

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class ConfigProperties(
    val dbConnectionString: String
)