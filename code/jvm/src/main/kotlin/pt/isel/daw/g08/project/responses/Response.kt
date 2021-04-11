package pt.isel.daw.g08.project.responses

import com.fasterxml.jackson.annotation.JsonIgnore

interface Response {
    @JsonIgnore
    fun getContentType(): String
}