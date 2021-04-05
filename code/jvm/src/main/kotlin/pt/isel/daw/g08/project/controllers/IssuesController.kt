package pt.isel.daw.g08.project.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

import java.util.Date

data class IssueOutputModel(
    val id: Int,
    val name: String,
    val description: String,
    val project: String,
    val state: String,
    val creationDate: Date,
    val closeDate: Date?,
)

data class IssuesOutputModel(
        val issues: List<IssueOutputModel>,
)

data class CommentOutputModel(
        val comment: String,
)

data class CommentsOutputModel(
        val comments: List<CommentOutputModel>,
)

data class IssuePostInputModel(
        val name: String,
        val description: String,
        val closeDate: Date?,
)

data class IssuePutInputModel(
        val name: String?,
        val description: String?,
        val state: String?,
        val closeDate: Date?,
)

data class IssuePostResponseModel(
        val createdId: String,
        val issueDetails: String,
)

data class IssuePutResponseModel(
        val status: String,             // Modified
        val issueDetails: String,
)

@RestController
@RequestMapping("/api/projects/{projectName}/issues")
class IssuesController {

    @GetMapping()
    fun getAllIssues(
            @PathVariable projectName: String,
    ): IssuesOutputModel {
        TODO()

    }

    @PostMapping()
    fun createIssue(
            @PathVariable projectName: String,
            @RequestBody input: IssuePostInputModel,
    ): IssuePostResponseModel {
        TODO()
    }

    @GetMapping("{issueId}")
    fun getIssue(
            @PathVariable projectName: String,
            @PathVariable issueId: Int,
    ): IssueOutputModel {
        TODO()
    }

    @PutMapping("{issueId}")
    fun editIssue(
            @PathVariable projectName: String,
            @PathVariable issueId: Int,
            @RequestBody input: IssuePutInputModel,
    ): IssuePutResponseModel {
        TODO()
    }

    @GetMapping("{issueId}/comments")
    fun getIssueComments(
            @PathVariable projectName: String,
            @PathVariable issueId: Int,
    ): CommentsOutputModel {
        TODO()
    }
}