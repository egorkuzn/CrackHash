package ru.nsu.fit.crackhash.manager.model.dto

data class WorkerResponseDto(
    val responseId: String,
    val value: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkerResponseDto

        if (responseId != other.responseId) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = responseId.hashCode()
        result = 31 * result + value.contentHashCode()
        return result
    }
}
