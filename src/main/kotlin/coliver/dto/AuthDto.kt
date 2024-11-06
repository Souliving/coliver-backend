package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val email: String,
    val password: String
)
