package coliver.routing.api.v1

import coliver.services.ImageService
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.text.toLong

fun Route.imageRouting() {
    val imageService by application.inject<ImageService>()

    route("/images", {
        tags("images")
    }) {
        get("/getImageByUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            val imgUrl = imageService.getImageByUserId(userId)
            if (imgUrl != null) {
                call.respond(imgUrl)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Problem with reading image")
            }
        }

        get("/getImageById/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]!!.toLong()

            val imgUrl = imageService.getImageLinkById(id)
            if (imgUrl != null) {
                call.respond(imgUrl)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Problem with reading image")
            }
        }

        post("/uploadImageByUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
                multipartBody {
                    mediaTypes(ContentType.Image.JPEG)
                    part<ByteArray>("img")
                }
            }
        }) {
            val img = call.receive<ByteArray>()
            val userId = call.parameters["userId"]!!.toLong()
            val imgId = imageService.uploadImageByUserId(img, userId)
            if (imgId != null) {
                call.respond(imgId.toString())
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Problem with uploading image")
            }
        }
    }
}
