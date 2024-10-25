package coliver

import coliver.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
}
