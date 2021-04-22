package pt.isel.daw.g08.project.database

enum class PsqlErrorCode(val code: String) {
    UniqueViolation("23505"),
    ForeignKeyViolation("23503"),
    NoStartState("P0005"),
    InvalidStateTransition("P0006"),
    ArchivedIssue("P0007"),
}