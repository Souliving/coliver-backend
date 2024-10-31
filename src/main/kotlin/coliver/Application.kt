package coliver

import coliver.plugins.configureDatabases
import coliver.plugins.configureKoin
import coliver.plugins.configureRouting
import coliver.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }
    configureSerialization()
    configureDatabases()
    configureKoin()
    configureRouting()
}


/*
* val client = HttpClient(Apache5) {
    engine {
    }
    install(ContentNegotiation) {
        json()
    }
}
val metro: List<Metro> = client.get("https://api.coliver.tech/api/v1/metro/").body()
* */
