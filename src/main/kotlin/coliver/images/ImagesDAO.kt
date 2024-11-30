package coliver_images.dao

import coliver_images.model.Image

interface ImagesDAO {
    suspend fun getImagesByUserId(userId: Long) : List<Image>
    suspend fun getImagesById(id: Long) : List<Image>
}
