package coliver.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@NoArg
@Serializable
data class Form(
    val id: Long? = null,
    val userId: Long? = null,
    val description: String,
    val rating: Double,
    val reviews: List<String>,
    val photoId: Long? = null,
    val propertiesId: Long? = null,
    val cityId: Long? = null,
    val budget: Long,
    val dateMove: LocalDateTime,
    val onlineDateTime: LocalDateTime
)

object Forms: Table("form") {
    val id = long("id").autoIncrement()
    val userId = long("user_id")
    val description = text("description")
    val rating = double("rating")
    val reviews = varchar("reviews", 64)
    val photoId = long("photo_id")
    val propertiesId = long("properties_id")
    val cityId = long("city_id")
    val budget = long("budget")
    val dateMove = datetime("date_move")
    val onlineDateTime = datetime("online_date_time")
}
