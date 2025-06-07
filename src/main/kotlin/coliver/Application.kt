package coliver

import coliver.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain
        .main(args)

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureKoin()
    configureMonitoring()
    configureRouting()
    configureAuthentication()
    configureCORS()
}
