package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePropertiesDto(
    val smoking: Boolean,
    val alcohol: Boolean,
    val petFriendly: Boolean,
    val isClean: Boolean,
    val homeOwnerId: Long? = null
)
