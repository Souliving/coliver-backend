package coliver.images

interface ImagesDAO {
    suspend fun getImagesByUserId(userId: Long) : List<Image>
    suspend fun getImagesById(id: Long) : List<Image>
    suspend fun getPackedImagesByIds(ids: List<Long>) : List<Image>
}
