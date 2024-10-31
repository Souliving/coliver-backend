package coliver.routing.api.v1

import coliver.dto.form.FilterDto
import coliver.services.ShortFormService
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.shortFormRouting() {

    val shortFormService by application.inject<ShortFormService>()

    route("/form", {
        tags("shortForms")
    }) {

        get("/getShortForms") {
            call.respond(shortFormService.getAll())
        }

        get("/getShortFormById/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]!!.toLong()
            call.respond(shortFormService.getById(id))
        }

        get("/getShortFormsForUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            call.respond(shortFormService.getForUser(userId))
        }

        post("/getShortFormsWithFilter/{userId}", {
            request {
                queryParameter<Long>("userId")
                body<FilterDto>()
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            val filter = call.receive<FilterDto>()
            call.respond(shortFormService.getWithFilter(userId, filter))
        }
    }
}
