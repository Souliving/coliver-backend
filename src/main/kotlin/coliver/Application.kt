package coliver

import coliver.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain
        .main(args)

fun Application.module() {
    configureAuthentication()
    configureSerialization()
    configureDatabases()
    configureKoin()
    configureMonitoring()
    configureRouting()
    configureCORS()
}
