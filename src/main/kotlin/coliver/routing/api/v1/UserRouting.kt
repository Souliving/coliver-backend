package coliver.routing.api.v1

import coliver.dto.FillUserDto
import coliver.services.UserService
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRouting() {
    val userService by application.inject<UserService>()

    route("/users", {
        tags("users")
    }) {
        get {
            call.respond(userService.getAll())
        }

        get("/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]!!.toLong()
            val user = userService.getById(id)
            call.respond(user)
        }

        post("/fillUser/{id}", {
            request {
                queryParameter<Long>("id")
                body<FillUserDto>()
            }
        }) {
            val id = call.parameters["id"]!!.toLong()
            val dto = call.receive<FillUserDto>()
            call.respond(HttpStatusCode.OK, userService.fillUser(id, dto))
        }
        get("/lkByUserId/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]!!.toLong()
            call.respond(userService.getLkById(id))
        }
    }
}
