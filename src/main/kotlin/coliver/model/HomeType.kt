package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class HomeType(
    var id: Long? = null,
    var name: String,
)

object HomeTypes : Table("home_type") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 64)
}
