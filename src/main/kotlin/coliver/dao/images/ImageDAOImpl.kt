package coliver.dao.images

import coliver.database.DatabaseFactory.dbQuery
import coliver.model.Image
import coliver.model.Images
import coliver.model.Images.bucketName
import coliver.model.Images.objectName
import coliver.model.Images.userId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class ImageDAOImpl : ImageDAO {
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

    override suspend fun addImage(image: Image): Long? =
        dbQuery {
            val insertStatement =
                Images.insert {
                    it[userId] = image.userId
                    it[bucketName] = image.bucketName
                    it[objectName] = image.objectName
                }
            insertStatement.resultedValues?.singleOrNull()?.let { resultRowToImage(it).id }
        }

    override suspend fun deleteImageById(id: Long): Boolean =
        dbQuery {
            Images.deleteWhere { Images.id eq id } != 0
        }
}
