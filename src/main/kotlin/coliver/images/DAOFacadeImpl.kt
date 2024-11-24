package coliver.dao

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Image
import coliver.model.Images
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DAOFacadeImpl : DAOFacade {

    private fun resultRowToImage(row: ResultRow) = Image(
        id = row[Images.id],
        userId = row[Images.userId],
        bucketName = row[Images.bucketName],
        objectName = row[Images.objectName],
    )

    override suspend fun getImagesByUserId(userId: Long) = dbQuery {
        transaction {
            Images.selectAll().where { Images.userId eq userId }.map(::resultRowToImage)
        }
    }

    override suspend fun getImagesById(id: Long): List<Image> = dbQuery {
        transaction {
            Images.selectAll().where { Images.id eq id }.map(::resultRowToImage)
        }
    }
}

val imageDAO: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
//        if (allImages().isEmpty()) {
//            val image = Image( userId = 2, bucketName = "images", objectName = "Жопа.jpg")
//            addImage(image)
//        }
    }
}
