package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class Metro(
    var id: Long? = null,
    var name: String,
    var cityId: Long? = null,
)

object Metros: Table("metro") {
    val id = long("id").autoIncrement()
    var name: Column<String> = varchar("name", 64)
    var cityId: Column<Long> = long("city_id")
}
