package coliver.routing.api.v1

import coliver.dto.CreateHomeOwnerDto
import coliver.services.HomeOwnerService
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.homeOwnerRouting() {
    val homeOwnerService by application.inject<HomeOwnerService>()

    route("/homeOwner", {
        tags("homeOwner")
    }) {

        get("/") {
            call.respond(homeOwnerService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLong()!!
            call.respond(homeOwnerService.getById(id))
        }

        get("/byHomeTypeId/{homeTypeId}") {
            val id = call.parameters["homeTypeId"]?.toLong()!!
            call.respond(homeOwnerService.getByHomeTypes(id))
        }

        post("/createHomeOwner", {
            request {
                body<CreateHomeOwnerDto>()
            }
        }) {
            val dto = call.receive<CreateHomeOwnerDto>()
            call.respond(HttpStatusCode.Created, homeOwnerService.insert(dto))
        }
    }
}
