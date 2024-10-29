package coliver.routing.api.v1

import coliver.services.UserService
import io.github.smiley4.ktorswaggerui.dsl.routing.route
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

        get("/{id}") {
            val id = call.parameters["id"]!!.toLong()
            val user = userService.getById(id)
            call.respond(user)
        }
    }
}
