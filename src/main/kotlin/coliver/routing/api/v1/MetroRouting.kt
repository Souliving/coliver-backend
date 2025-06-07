package coliver.routing.api.v1

import coliver.services.MetroService
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.metroRouting() {
    val metroService by application.inject<MetroService>()

    route("/metro", {
        tags("metro")
    }) {
        get {
            call.respond(metroService.all())
        }

        get("/{metroId}", {
            request {
                queryParameter<Long>("metroId")
            }
        }) {
            val metroId = call.parameters["metroId"]!!.toLong()
            call.respond(metroService.getMetroById(metroId))
        }

        get("/getAllMetroByCityId/{cityId}", {
            request {
                queryParameter<Long>("cityId")
            }
        }) {
            val cityId = call.parameters["cityId"]!!.toLong()
            call.respond(metroService.getMetroByCityId(cityId))
        }

        get("/getMetroByName/{name}", {
            request {
                queryParameter<String>("name")
            }
        }) {
            val metroName = call.parameters["name"]!!
            call.respond(metroService.getMetroByName(metroName))
        }
    }
}
