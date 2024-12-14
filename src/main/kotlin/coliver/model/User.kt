package coliver.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@NoArg
@Serializable
data class User(
    var id: Long? = null,
    val email: String,
    val password: String,
    val role: UserRole,
    val name: String? = null,
    val age: Long,
    val gender: Gender? = null,
    val enabled: Boolean,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
)

enum class UserRole {
    USER,
    ADMIN
}

enum class Gender {
    MALE,
    FEMALE
}

object Users : Table("users") {
    val id = long("id").autoIncrement()
    val email = varchar("email", 256)
    val password = varchar("password", 2048)
    val role = varchar("role", 255)
    val name = varchar("name", 64)
    val age = long("age")
    val gender = varchar("gender", 64)
    val enabled = bool("enabled")
    val createDate = datetime("create_date")
    val modifyDate = datetime("modify_date")
}
