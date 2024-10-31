package coliver.dto.form

import kotlinx.serialization.Serializable

@Serializable
data class MetroFormDto(
    var formId: Long,
    var metroIds: List<Long>,
)
