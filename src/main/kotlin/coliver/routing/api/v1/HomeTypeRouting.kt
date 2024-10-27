package coliver.routing.api.v1

import coliver.services.HomeTypeService
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.homeTypeRouting() {
    val homeTypeService by application.inject<HomeTypeService>()

    route("/homeType") {
        get("/") {
            call.respond(homeTypeService.getHomeTypes())
        }

        get("/getHomeTypeById/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()!!
            call.respond(homeTypeService.getHomeTypeById(id))
        }
    }
}
