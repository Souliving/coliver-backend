package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class HomeOwner(
    var id: Long? = null,
    var metroId: Long? = null,
    val cityId: Long? = null,
    var homeTypeId: Long? = null,
    var description: String,
    var photoId: Long? = null,
)

object HomeOwners : Table("home_owner") {
    val id = long("id").autoIncrement()
    val metroId = long("metro_id")
    val cityId = long("city_id")
    val homeTypeId = long("home_type_id")
    val description = text("description")
    val photoId = long("photo_id")
}
