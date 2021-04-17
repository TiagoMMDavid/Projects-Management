package pt.isel.daw.g08.project.database

enum class PsqlErrorCode(val code: String) {
    UniqueViolation("23505"),
}