package coliver.routing.api.v1

import coliver.dao.metro.metroDao
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.metroRouting() {
    route("/metro") {
        get {
            call.respond(metroDao.getAll())
        }
    }
}
