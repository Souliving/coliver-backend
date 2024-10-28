package coliver.routing.api.v1

import coliver.services.MetroService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

val client = HttpClient(CIO)
fun Route.metroRouting() {
    val metroService by application.inject<MetroService>()

    route("/metro") {

        get {
            call.respond(metroService.all())
        }

        get("/{metroId}") {
            val metroId = call.parameters["metroId"]!!.toLong()
            call.respond(metroService.getMetroById(metroId))
        }

        get("/getAllMetroByCityId/{cityId}") {
            val cityId = call.parameters["cityId"]!!.toLong()
            call.respond(metroService.getMetroByCityId(cityId))
        }

        get("/getMetroByName/{name}") {
            val metroName = call.parameters["name"]!!
            call.respond(metroService.getMetroByName(metroName))
        }
    }
}
