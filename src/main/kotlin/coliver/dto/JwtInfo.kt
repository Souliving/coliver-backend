package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class JwtInfo(
    val userId: Long,
    val email: String,
    val username: String,
    val role: String,
)
