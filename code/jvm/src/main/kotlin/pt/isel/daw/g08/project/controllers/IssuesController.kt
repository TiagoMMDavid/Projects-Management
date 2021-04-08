package pt.isel.daw.g08.project.controllers

import org.jdbi.v3.core.Jdbi
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import pt.isel.daw.g08.project.controllers.models.*

@RestController
@RequestMapping("/api/projects/{projectName}/issues")
class IssuesController(val jdbi: Jdbi) {

    @GetMapping
    fun getAllIssues(
        @PathVariable projectName: String,
    ): IssuesOutputModel {
        TODO()
    }

    @GetMapping("{issueId}")
    fun getIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
    ): IssueOutputModel {
        TODO()
    }

    @PostMapping
    fun createIssue(
        @PathVariable projectName: String,
        @RequestBody input: IssueCreateInputModel,
    ): IssueCreateResponseModel {
        TODO()
    }

    @PutMapping("{issueId}")
    fun editIssue(
        @PathVariable projectName: String,
        @PathVariable issueId: Int,
        @RequestBody input: IssueEditInputModel,
    ): IssueEditResponseModel {
        TODO()
    }
}