package coliver.routing.api.v1

import coliver.services.FormService
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.formRouting() {
    val formService by application.inject<FormService>()

    route("/form", {
        tags("forms")
    }) {
        get {
            call.respond(formService.getAll())
        }

        get("/getFormById/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(formService.getById(id))
        }

        get("/getFormByUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
            }
        }) {
            val userId = call.parameters["userId"]?.toLong()
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(formService.getByUserId(userId))
        }

        get("/getFullFormById/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]?.toLong()
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(formService.getFullFormById(id))
        }
    }
}
