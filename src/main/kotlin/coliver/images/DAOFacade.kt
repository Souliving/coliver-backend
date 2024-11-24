package coliver.dao

import coliver.model.Image

interface DAOFacade {
    suspend fun getImagesByUserId(userId: Long) : List<Image>
    suspend fun getImagesById(id: Long) : List<Image>
}
