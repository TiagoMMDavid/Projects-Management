package pt.isel.daw.g08.project.controllers.models

data class LabelOutputListModel(
    val name: String,
)

data class LabelsOutputModel(
    val projectUrl: String,
    val labels: List<LabelOutputListModel>,
)

data class LabelInputModel(
    val name: String,
)

data class LabelCreateOutputModel(
    val status: String,             // Created
    val labelDetails: String,
)

data class LabelDeleteOutputModel(
    val status: String,             // Deleted
)