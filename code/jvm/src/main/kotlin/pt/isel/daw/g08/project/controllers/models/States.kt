package pt.isel.daw.g08.project.controllers.models

data class StateOutputModel(
    val name: String,
    val isStartState: Boolean,
    val nextStates: List<String>,
)

data class StatesOutputModel(
    val states: List<StateOutputModel>,
)

data class StateInputModel(
    val isStartState: Boolean?,
    val nextStates: List<String>?,
)

data class StateCreateOutputModel(
    val status: String,             // Created or Modified
    val stateDetails: String,
)

data class StateDeleteOutputModel(
    val status: String,             // Deleted
)
