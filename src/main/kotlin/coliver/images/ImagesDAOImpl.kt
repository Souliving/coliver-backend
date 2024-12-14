package coliver.images

import coliver.database.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class ImagesDaoImpl : ImagesDAO {
    private fun resultRowToImage(row: ResultRow) =
        Image(
            id = row[Images.id],
            userId = row[Images.userId],
            bucketName = row[Images.bucketName],
            objectName = row[Images.objectName],
        )

    override suspend fun getImagesByUserId(userId: Long) =
        dbQuery {
            Images.selectAll().where { Images.userId eq userId }.map(::resultRowToImage)
        }

    override suspend fun getImagesById(id: Long): List<Image> =
        dbQuery {
            Images.selectAll().where { Images.id eq id }.map(::resultRowToImage)
        }

    override suspend fun getPackedImagesByIds(ids: List<Long>): List<Image> =
        dbQuery {
            Images.selectAll().where { Images.id inList ids }.map(::resultRowToImage)
        }
}

val imageDAO: ImagesDAO =
    ImagesDaoImpl().apply {
        runBlocking {
//        if (allImages().isEmpty()) {
//            val image = Image( userId = 2, bucketName = "images", objectName = "Жопа.jpg")
//            addImage(image)
//        }
        }
    }
