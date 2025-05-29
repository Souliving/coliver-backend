package coliver.dao.images

import coliver.model.Image

interface ImageDAO {
    suspend fun getImagesByUserId(userId: Long): List<Image>

    suspend fun getImagesById(id: Long): List<Image>

    suspend fun addImage(image: Image): Long?

    suspend fun deleteImageById(id: Long): Boolean

    suspend fun getPackedImagesByIds(ids: List<Long>): List<Image>
}
