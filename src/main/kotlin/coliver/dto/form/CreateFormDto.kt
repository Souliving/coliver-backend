package coliver.dto.form

import coliver.dto.CreatePropertiesDto
import kotlinx.serialization.KSerializer

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Serializable
data class CreateFormDto(
    var userId: Long? = null,
    var description: String,
    var homeTypesIds: List<Long>? = null,
    var rating: Double,
    var reviews: List<String>,
    var photoId: Long? = null,
    var properties: CreatePropertiesDto,
    var cityId: Long? = null,
    var metroIds: List<Long>? = null,
    var budget: Long,
    @Serializable(with = DateSerializer::class)
    var dateMove: LocalDateTime,
    @Serializable(with = DateSerializer::class)
    var onlineDateTime: LocalDateTime
)

@Serializer(forClass = LocalDateTime::class)
object DateSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
