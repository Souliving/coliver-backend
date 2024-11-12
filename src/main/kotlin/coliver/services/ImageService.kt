package coliver.services

import coliver.dao.imageDAO
import coliver.plugins.minio

class ImageService {

    suspend fun getImageLinkById(id: Long): String {
        val paths = imageDAO.getImagesById(id)
        val info = paths.first()
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        return imgUrl!!
    }
}
