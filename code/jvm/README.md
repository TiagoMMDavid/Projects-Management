# Build and Run

## Requirements
In order to be able to both build and run this project, you need the following software installed in your machine:
- [Docker](https://www.docker.com/get-started)
- [Java 11 SDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

## Steps
The following steps assume you are in the same directory as this README file.

1. (Optional) Edit the `docker-compose.yml` [file](docker-compose.yml) to change the database user, password, name and port. If you change the port, remember to expose it in the correponding `Dockerfile`, present in the [tests directory](tests)
3. (Optional) Edit the `application.properties` [file](src/main/resources/application.properties) with the desired PostgreSQL connection string
4. (Optional) Run the application's tests by executing the `gradlew test` command
5. Execute the `gradlew bootRun` command to start the Spring Boot application
