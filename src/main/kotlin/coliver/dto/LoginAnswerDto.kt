package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginAnswerDto(
    val jwt: JwtDto,
    val email: String,
    val name: String
)

@Serializable
data class JwtDto(
    val userId: Long,
    val token: String,
    val refresh: String
)
