package coliver.dto

import kotlinx.serialization.Serializable

@Serializable
data class LkInfoDto(
    var formId: Long,
    var cityName: String,
    var metroNames: List<String>
)
