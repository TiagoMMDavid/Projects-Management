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
	fun getJdbi() : Jdbi {
		val jdbi = Jdbi.create("jdbc:postgresql://localhost:5433/daw-db", "daw", "123macaco")
			.installPlugin(KotlinPlugin())

		jdbi
			.registerRowMapper(IssueDao::class.java) { rs, ctx -> IssueDao.mapRow(rs, ctx, jdbi) }
			.registerRowMapper(StateDao::class.java) { rs, ctx -> StateDao.mapRow(rs, ctx, jdbi) }
		return jdbi
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
