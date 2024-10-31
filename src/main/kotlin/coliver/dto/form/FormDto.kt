package coliver.dto.form

import coliver.model.HomeType
import kotlinx.serialization.Serializable

@Serializable
data class FormDto(
    val id: Long,
    val homeType: List<HomeType>,
    val socialMedia: Long? = null,
    val rating: Double,
    val reviews: List<String>
)
