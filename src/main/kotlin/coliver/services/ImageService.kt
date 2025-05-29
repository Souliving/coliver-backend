package coliver.services

import coliver.dao.images.ImageDAO
import coliver.dto.ImagePack
import coliver.images.getImgName
import coliver.images.minio
import coliver.model.Image
import com.google.common.io.ByteSource

class ImageService(
    private val imageDAO: ImageDAO,
) {
    suspend fun getImageByUserId(userId: Long): String? {
        val paths = imageDAO.getImagesByUserId(userId)
        if (paths.isEmpty()) {
            return null
        }
        val info = paths.first()
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        return imgUrl
    }

    suspend fun getImageLinkById(id: Long): String? {
        val paths = imageDAO.getImagesById(id)
        if (paths.isEmpty()) {
            return null
        }
        val info = paths.first()
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        return imgUrl
    }

    suspend fun getImageLinkPack(ids: List<Long>): HashMap<Long, ImagePack> {
        val paths = imageDAO.getPackedImagesByIds(ids)
        if (paths.isEmpty()) {
            return hashMapOf()
        }
        val map = hashMapOf<Long, ImagePack>()

        paths.forEach {
            map[it.id!!] = ImagePack(it.bucketName, it.objectName)
        }

        map.forEach { (id, pack) ->
            pack.imgLink = minio.getPresignedUrl(pack.bucketName, pack.objectName)
        }
        return map
    }

    suspend fun uploadImageByUserId(
        img: ByteArray,
        userId: Long
    ): Long? {
        val imgName = getImgName("jpg")
        val imgId =
            imageDAO.addImage(
                Image(
                    userId = userId,
                    bucketName = "images",
                    objectName = imgName,
                ),
            )
        val stream = ByteSource.wrap(img).openStream()
        minio.putObject("images", imgName, stream)
        return imgId
    }
}
