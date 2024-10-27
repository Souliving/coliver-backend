package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class City(
    var id: Long? = null,
    var name: String,
)

object Cities: Table("city") {
    var id = long("id").autoIncrement()
    var name: Column<String> = varchar("name", 64)
}
