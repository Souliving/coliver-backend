package coliver_images.dao

import coliver.database.DatabaseFactory.dbQuery
import coliver_images.model.Image
import coliver_images.model.Images
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class DAOFacadeImpl : ImagesDAO {

    private fun resultRowToImage(row: ResultRow) = Image(
        id = row[Images.id],
        userId = row[Images.userId],
        bucketName = row[Images.bucketName],
        objectName = row[Images.objectName],
    )

    override suspend fun getImagesByUserId(userId: Long) = dbQuery {
        Images.selectAll().where { Images.userId eq userId }.map(::resultRowToImage)
    }

    override suspend fun getImagesById(id: Long): List<Image> = dbQuery {
        Images.selectAll().where { Images.id eq id }.map(::resultRowToImage)
    }


}

val dao: ImagesDAO = DAOFacadeImpl().apply {
    runBlocking {
//        if (allImages().isEmpty()) {
//            val image = Image( userId = 2, bucketName = "images", objectName = "Жопа.jpg")
//            addImage(image)
//        }
    }
}
