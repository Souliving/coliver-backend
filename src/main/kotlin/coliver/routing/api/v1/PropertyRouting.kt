package coliver.routing.api.v1

import coliver.services.PropertyService
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.propertyRouting() {
    val propertyService by application.inject<PropertyService>()

    route("/properties", {
        tags("property")
    }) {
        get("/{id}") {
            val id = call.parameters["id"]!!.toLong()
            call.respond(propertyService.getById(id))
        }
    }
}
