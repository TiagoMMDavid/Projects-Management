package pt.isel.daw.g08.project

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.isel.daw.g08.project.utils.EnvironmentInfo

@ConfigurationPropertiesScan
@SpringBootApplication
class Application(private val configProperties: ConfigProperties) {
	@Bean
	fun getJdbi() = Jdbi
		.create(configProperties.dbConnectionString)
		.installPlugin(KotlinPlugin())

	@Bean
	fun getEnv() = EnvironmentInfo()
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
