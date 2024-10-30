package coliver.dto

import coliver.model.Gender
import kotlinx.serialization.Serializable

@Serializable
data class FillUserDto(
    val name: String,
    val email: String,
    val age: Long,
    val gender: Gender
)
