package coliver.routing.api.v1

import coliver.dto.form.FavFormDto
import coliver.services.FavFormService
import io.github.smiley4.ktorswaggerui.dsl.routing.delete
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.favFormRouting() {

    val favFormService by application.inject<FavFormService>()


    route("/form", {
        tags("favForms")
    }) {

        get("/getFavoriteFormsByUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            call.respond(favFormService.getFavForms(userId))
        }

        put("/addFavoriteForm", {
            request {
                body<FavFormDto>()
            }
        }) {
            val fav = call.receive<FavFormDto>()
            call.respond(favFormService.add(fav.userId, fav.favFormId))
        }

        delete("/deleteFavoriteForm", {
            request {
                body<FavFormDto>()
            }
        }) {
            val fav = call.receive<FavFormDto>()
            call.respond(favFormService.delete(fav.userId, fav.favFormId))
        }
    }

}
