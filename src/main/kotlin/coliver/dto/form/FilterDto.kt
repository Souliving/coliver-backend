package coliver.dto.form

import kotlinx.serialization.Serializable

@Serializable
data class FilterDto(
    val price: PriceFilterDto,
    val age: AgeFilterDto,
    val cityId: List<Long>,
    val metroIds: List<Long>,
    val smoking: Boolean?,
    val alcohol: Boolean?,
    val petFriendly: Boolean?,
    val isClean: Boolean?
)

@Serializable
data class AgeFilterDto(
    val startAge: Int? = null,
    val endAge: Int? = null,
)

@Serializable
data class PriceFilterDto(
    val startPrice: Long? = null,
    val endPrice: Long? = null
)
