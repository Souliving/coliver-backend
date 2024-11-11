package coliver.dto.form

import coliver.model.City
import coliver.model.Metro
import coliver.model.Property
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class ShortFormDto(
    val id: Long? = null,
    val name: String,
    val age: Int,
    val city: City? = null,
    val metro: List<Metro>,
    val budget: Long,
    val description: String,
    val dateMove: LocalDateTime?,
    val properties: Property? = null,
    val photoId: Long? = null,
    val onlineDateTime: LocalDateTime?,
    val isFavorite: Boolean = false,
    var imageLink: String? = null,
)
