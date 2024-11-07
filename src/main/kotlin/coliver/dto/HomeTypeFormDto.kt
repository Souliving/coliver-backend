package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class HomeTypeFormDto(
    val formId: Long,
    val homeTypeIds: List<Long>
)
