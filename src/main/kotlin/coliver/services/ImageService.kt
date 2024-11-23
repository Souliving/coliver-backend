package coliver.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.request.*

class ImageService {

//    suspend fun getImageLinkById(id: Long): String {
//        val paths = imageDAO.getImagesById(id)
//        val info = paths.firstOrNull() ?: return "no image"
//        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
//        return imgUrl!!
//    }

    private val imageAPI = "http://94.103.89.23:9090"

    private val client = HttpClient(Apache5)

    suspend fun getImageLinkById(id: Long): String {
        var link = ""

        val extraPath = "getImageById/$id"
        link = client.get("$imageAPI/$extraPath").body<String>()

        return link
    }
}
