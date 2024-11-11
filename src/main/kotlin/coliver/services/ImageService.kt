package coliver.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.time.measureTime

class ImageService {

    private val imageAPI = "https://images.coliver.tech"

    val client = HttpClient(Apache5) {
        engine {
        }
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getImageLinkById(id: Long): String {
        var link = ""
        val time = measureTime {
            val extraPath = "getImageById/$id"
            link = client.get("$imageAPI/$extraPath").body<String>()
        }
        println(time)

        return link
    }
}
