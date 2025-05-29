package coliver.services

import coliver.dao.images.ImageDAO
import coliver.dto.ImagePack
import coliver.images.getImgName
import coliver.images.minio
import coliver.model.Image
import coliver.plugins.imgCaffeine
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
        val cachedImgLink = imgCaffeine.getIfPresent(info.id!!)
        if (cachedImgLink != null) {
            return cachedImgLink
        }
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        imgCaffeine.put(info.id!!, imgUrl)
        return imgUrl
    }

    suspend fun getImageLinkById(id: Long): String? {
        val cachedImgLink = imgCaffeine.getIfPresent(id)
        if (cachedImgLink != null) {
            return cachedImgLink
        }
        val paths = imageDAO.getImagesById(id)
        if (paths.isEmpty()) {
            return null
        }
        val info = paths.first()
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        imgCaffeine.put(id, imgUrl)
        return imgUrl
    }

    suspend fun getImageLinkPack(ids: List<Long>): HashMap<Long, ImagePack> {
        val cachedImgLinks = hashMapOf<Long, ImagePack>()
        val nonCachedIds = arrayListOf<Long>()
        ids.forEach { id ->
            val imgLink = imgCaffeine.getIfPresent(id)
            if (imgLink != null) {
                cachedImgLinks[id] = ImagePack("", "", imgLink)
            } else {
                nonCachedIds.add(id)
            }
        }

        if (nonCachedIds.isEmpty()) {
            return cachedImgLinks
        }

        val paths = imageDAO.getPackedImagesByIds(nonCachedIds)
        val map = hashMapOf<Long, ImagePack>()

        paths.forEach {
            map[it.id!!] = ImagePack(it.bucketName, it.objectName)
        }

        map.forEach { (id, pack) ->
            val newImgLink = minio.getPresignedUrl(pack.bucketName, pack.objectName)
            pack.imgLink = newImgLink
            imgCaffeine.put(id, newImgLink!!)
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
