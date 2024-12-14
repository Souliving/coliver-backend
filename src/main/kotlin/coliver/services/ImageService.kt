package coliver.services

import coliver.dto.ImagePack
import coliver.images.imageDAO
import coliver.images.minio
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder

class ImageService {
    private val imageAPI = "http://94.103.89.23:9090"

    private val client =
        HttpClient(Apache5) {
            engine {
                connectTimeout = 10_000
                connectionRequestTimeout = 20_000
                customizeClient {
                    setConnectionManager(
                        PoolingAsyncClientConnectionManagerBuilder
                            .create()
                            .setMaxConnTotal(10_000)
                            .setMaxConnPerRoute(500)
                            .build(),
                    )
                }
            }
            install(ContentNegotiation) {
                json()
            }
        }

    suspend fun getImageLinkByIdByWeb(id: Long): String {
        val extraPath = "getImageById/$id"
        val link = client.get("$imageAPI/$extraPath").body<String>()
        return link
    }

    suspend fun getImageLinkById(id: Long): String {
        val paths = imageDAO.getImagesById(id)
        if (paths.isEmpty()) {
            return "not found"
        }
        val info = paths.first()
        val imgUrl = minio.getPresignedUrl(info.bucketName, info.objectName)
        return imgUrl ?: "not found"
    }

    suspend fun getImageLinkPack(ids: List<Long>): HashMap<Long, ImagePack> {
        val paths = imageDAO.getPackedImagesByIds(ids)
        if (paths.isEmpty()) {
            return hashMapOf()
        }
        val map = hashMapOf<Long, ImagePack>()

        paths.forEach {
            map[it.id as Long] = ImagePack(it.bucketName, it.objectName)
        }

        map.forEach { (id, pack) ->
            pack.imgLink = minio.getPresignedUrl(pack.bucketName, pack.objectName)
        }
        return map
    }

    suspend fun getLinksForIds(ids: List<Long>): HashMap<Long, String> {
        val extraPath = "assembleImgLinks"
        val links =
            client
                .post("$imageAPI/$extraPath") {
                    contentType(ContentType.Application.Json)
                    setBody(ids)
                }.body<HashMap<Long, String>>()
        return links
    }
}
