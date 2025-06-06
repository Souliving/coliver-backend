package coliver.dto

import coliver.model.Gender
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    val email: String,
    var password: String,
    val name: String,
    val age: Long = 0,
    val gender: Gender,
    val phone: String
)
