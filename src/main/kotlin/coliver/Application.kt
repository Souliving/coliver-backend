package coliver

import coliver.plugins.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()

    install(Authentication) {
        jwt("userAuth") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build())
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != ""
//                    && credential.payload.getClaim("role").asString() == "USER"
                    && credential.payload.getClaim("refresh").isMissing) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
    configureSerialization()
    configureDatabases()
    configureKoin()
    configureMonitoring()
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
