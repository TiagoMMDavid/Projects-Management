package pt.isel.daw.g08.project.controllers.models

import pt.isel.daw.g08.project.responses.siren.SirenClass.home
import java.time.OffsetDateTime

class HomeOutputModel(
    val name: String,
    val group: String,
    val uptimeMs: Long,
    val time: OffsetDateTime,
) : OutputModel() {

    override fun getSirenClasses() = listOf(home)
}