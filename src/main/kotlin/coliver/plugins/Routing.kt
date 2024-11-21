package coliver.plugins

import coliver.routing.api.v1.*
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthKeyLocation
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.github.smiley4.ktorswaggerui.data.kotlinxExampleEncoder
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting() {
    routing {
        install(SwaggerUI) {
            url {
                port = 8085
            }
            info {
                title = "Example API"
                version = "latest"
                description = "Example API for testing and demonstration purposes."
            }
            server {
                description = "Development Server"
            }
            security {
                securityScheme("MySecurityScheme") {
                    type = AuthType.HTTP
                    location = AuthKeyLocation.HEADER
                    bearerFormat = "jwt"
                    scheme = AuthScheme.BEARER
                }
                defaultSecuritySchemeNames("MySecurityScheme")
                // if no other response is documented for "401 Unauthorized", this information is used instead
                defaultUnauthorizedResponse {
                    description = "Username or password is invalid"
                }
            }
            examples {
                exampleEncoder = kotlinxExampleEncoder
            }
        }

        route("api.json") {
            openApiSpec()
        }

        route("swagger") {
            swaggerUI("/api.json")
        }


        routing {
            get("/metrics") {
                call.respond(appMicrometerRegistry.scrape())
            }
            get("/") {
                call.respondRedirect("/swagger")
            }
        }
        route("api/v1") {
            authRouting()
            userRouting()
            formRouting()
            shortFormRouting()
            favFormRouting()
            metroRouting()
            cityRouting()
            homeTypeRouting()
            homeOwnerRouting()
            propertyRouting()
        }
    }
}
