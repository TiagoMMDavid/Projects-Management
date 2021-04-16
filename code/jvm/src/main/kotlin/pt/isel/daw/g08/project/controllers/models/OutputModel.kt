package pt.isel.daw.g08.project.controllers.models

import com.fasterxml.jackson.annotation.JsonIgnore
import pt.isel.daw.g08.project.responses.siren.Siren
import pt.isel.daw.g08.project.responses.siren.SirenAction
import pt.isel.daw.g08.project.responses.siren.SirenClass
import pt.isel.daw.g08.project.responses.siren.SirenLink

abstract class OutputModel {

    @JsonIgnore
    abstract fun getSirenClasses(): List<SirenClass>

    fun toSirenObject(
        rel: List<String>? = null,
        entities: List<Any>? = null,
        actions: List<SirenAction>? = null,
        links: List<SirenLink>
    ) = Siren(
        clazz = getSirenClasses(),
        rel = rel,
        this,
        entities = entities,
        actions = actions,
        links = links
    )
}