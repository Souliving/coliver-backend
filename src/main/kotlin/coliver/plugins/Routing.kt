package coliver.plugins

import coliver.routing.api.v1.cityRouting
import coliver.routing.api.v1.metroRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting() {
    routing {
        swaggerUI("swagger") {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
                port = 8080
            }

        }
        route("api/v1") {
            metroRouting()
            cityRouting()
        }
    }
}
