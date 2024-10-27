package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class Property(
    var id: Long? = null,
    var smoking: Boolean,
    var alcohol: Boolean,
    var petFriendly: Boolean,
    var isClean: Boolean,
    var homeOwnerId: Long? = null
)

object Properties : Table("properties") {
    val id = long("id").autoIncrement()
    val smoking = bool("smoking")
    val alcohol = bool("alcohol")
    val petFriendly = bool("pet_friendly")
    val isClean = bool("is_clean")
    val homeOwnerId = long("home_owner_id")
}
