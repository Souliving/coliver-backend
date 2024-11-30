package coliver.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class FormMetroId(
    var id: Long? = null,
    var formId: Long,
    var metroId: Long
)

object FormMetroIds: Table("form_metro_ids") {
    var id = long("id").autoIncrement()
    var formId = long("form_id")
    var metroId = long("metro_id")
}
