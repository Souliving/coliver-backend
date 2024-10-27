package coliver.routing.api.v1

import coliver.services.PropertyService
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.propertyRouting() {
    val propertyService by application.inject<PropertyService>()

    route("/properties") {
        get("/{id}") {
            val id = call.parameters["id"]!!.toLong()
            call.respond(propertyService.getById(id))
        }
    }
}
