package coliver.dto.form

import kotlinx.serialization.Serializable

@Serializable
data class FavFormDto(
    val userId: Long,
    val favFormId: Long
)
