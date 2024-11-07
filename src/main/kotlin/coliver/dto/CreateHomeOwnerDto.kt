package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateHomeOwnerDto(
    var metroId: Long? = null,
    val cityId: Long? = null,
    var homeTypeId: Long? = null,
    var description: String,
    var photoId: Long? = null,
)
