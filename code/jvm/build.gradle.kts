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

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	runtimeOnly("org.postgresql:postgresql")
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

task<Exec>("dbTestsUp") {
	commandLine("docker-compose", "up", "-d", "daw-db")
}

task<Exec>("dbTestsWait") {
	commandLine("docker", "exec", "daw-db", "/app/bin/wait-for-postgres.sh", "localhost")
	dependsOn("dbTestsUp")
}

task<Exec>("dbTestsDown") {
	commandLine("docker-compose", "down")
}

tasks {
	named<Test>("test") {
		dependsOn("dbTestsWait")
	}
}