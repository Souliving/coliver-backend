package coliver_images.model

import coliver.model.NoArg
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@NoArg
@Serializable
data class Image(
    var id: Long? = null,
    var userId: Long,
    var bucketName: String,
    var objectName: String
)

object Images: Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val userId: Column<Long> = long("user_id")
    var bucketName: Column<String> = varchar("bucket_name", 255)
    var objectName: Column<String> = varchar("object_name", 512)
}
