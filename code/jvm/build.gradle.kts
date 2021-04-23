import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.spring") version "1.4.31"
}

group = "pt.isel.daw.g08"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val DOCKER_PROJECT_NAME = "DAW"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.postgresql:postgresql:42.2.19")
	implementation("org.jdbi:jdbi3-core:3.18.0")
	implementation("org.jdbi:jdbi3-kotlin:3.18.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<Exec>("dbUp") {
	commandLine("docker-compose", "-p", DOCKER_PROJECT_NAME, "up", "-d", "daw-db")
}

task<Exec>("dbWait") {
	commandLine("docker", "exec", "daw-db", "/app/bin/wait-for-postgres.sh", "localhost")
	dependsOn("dbUp")
}

task<Exec>("dbStop") {
	commandLine("docker", "stop", "daw-db")
}

task<Exec>("dbTestsUp") {
	commandLine("docker-compose", "-p", DOCKER_PROJECT_NAME, "up", "-d", "daw-db-tests")
}

task<Exec>("dbTestsWait") {
	commandLine("docker", "exec", "daw-db-tests", "/app/bin/wait-for-postgres.sh", "localhost")
	dependsOn("dbTestsUp")
}

task<Exec>("dbTestsDown") {
	commandLine("docker-compose", "-p", DOCKER_PROJECT_NAME, "rm", "-fsv", "daw-db-tests")
}

tasks {
	named<Test>("test") {
		dependsOn("dbTestsWait")
		finalizedBy("dbTestsDown")
	}
	
	named("bootRun") {
		dependsOn("dbWait")
		finalizedBy("dbStop")
	}
}