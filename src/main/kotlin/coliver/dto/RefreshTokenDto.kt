package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    val token: String
)
