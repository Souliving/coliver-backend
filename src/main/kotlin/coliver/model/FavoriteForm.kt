package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class FavoriteForm(
    var id: Long? = null,
    var userId: Long,
    var favFormId: Long,
)

object FavoriteForms : Table("favorite_forms") {
    var id = long("id").autoIncrement()
    var userId = long("user_id")
    var favFormId = long("fav_form_id")
}
