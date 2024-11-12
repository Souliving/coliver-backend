package coliver.dao

import coliver.model.Image

interface DAOFacade {
    suspend fun allImages() : List<Image>
    suspend fun getImagesByUserId(userId: Long) : List<Image>
    suspend fun getImagesById(id: Long) : List<Image>
    suspend fun addImage(image: Image) : Long?
    suspend fun deleteImageById(id: Long): Boolean
}
