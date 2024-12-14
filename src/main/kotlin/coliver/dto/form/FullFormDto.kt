package coliver.dto.form

import coliver.model.City
import coliver.model.HomeType
import coliver.model.Metro
import coliver.model.Property
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FullFormDto(
    var id: Long? = null,
    var userId: Long? = null,
    var name: String,
    val age: Int,
    var city: City? = null,
    var metro: List<Metro>,
    var budget: Long,
    var description: String,
    var dateMove: LocalDateTime?,
    var properties: Property? = null,
    var photoId: Long? = null,
    var onlineDateTime: LocalDateTime?,
    var homeType: List<HomeType>? = null,
    var socialMediaIds: List<Long>? = null,
    var rating: Double,
    var reviews: List<String>
)
