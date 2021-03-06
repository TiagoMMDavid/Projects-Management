package pt.isel.daw.g08.project

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.daw.g08.project.pipeline.argumentresolvers.PaginationResolver
import pt.isel.daw.g08.project.pipeline.argumentresolvers.UserResolver
import pt.isel.daw.g08.project.pipeline.interceptors.AuthorizationInterceptor

@ConfigurationPropertiesScan
@SpringBootApplication
class Application(private val configProperties: ConfigProperties) {
	@Bean
	fun getJdbi() = Jdbi
		.create(configProperties.dbConnectionString)
		.installPlugin(KotlinPlugin())
}

@Component
class MvcConfig(val authInterceptor: AuthorizationInterceptor) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(authInterceptor)
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(PaginationResolver())
		resolvers.add(UserResolver())
	}
}

fun main(args: Array<String>) {
	System.setProperty("server.port", Routes.PORT)
	runApplication<Application>(*args)
}
