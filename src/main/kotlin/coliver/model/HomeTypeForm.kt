package coliver.model

import org.jetbrains.exposed.sql.Table

data class HomeTypeForm(
    val id: Long? = null,
    val formId: Long,
    val homeTypeId: Long
)

object HomeTypeForms: Table("home_type_form") {
    var id = long("id").autoIncrement()
    var formId = long("form_id")
    var homeTypeId = long("home_type_id")
}
