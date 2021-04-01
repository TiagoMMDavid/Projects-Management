package pt.isel.daw.g08.project

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.isel.daw.g08.project.dao.IssueDao
import pt.isel.daw.g08.project.dao.StateDao

@SpringBootApplication
class Application {
	@Bean
	fun getJdbi() = Jdbi.create("jdbc:postgresql://localhost:5432/changeit", "changeit", "changeit")
			.installPlugin(KotlinPlugin())
			.registerRowMapper(IssueDao::class.java, IssueDao::getRowMapper)
			.registerRowMapper(StateDao::class.java, StateDao::getRowMapper)
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
