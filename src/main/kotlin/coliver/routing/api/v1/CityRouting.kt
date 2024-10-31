package coliver.routing.api.v1

import coliver.services.CityService
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.cityRouting() {
    val cityService by application.inject<CityService>()

    route("/cities", {
        tags("cities")
    }) {

        get("/") {
            call.respond(cityService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()!!
            call.respond(cityService.getCityById(id))
        }
    }
}
