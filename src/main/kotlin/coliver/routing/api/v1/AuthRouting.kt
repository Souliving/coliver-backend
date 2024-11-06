package coliver.routing.api.v1

import at.favre.lib.crypto.bcrypt.BCrypt
import coliver.dto.*
import coliver.services.UserService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*

import io.ktor.server.response.*
import io.ktor.server.routing.*


import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.security.SecureRandom
import java.util.*

fun Route.authRouting() {

    //println(user.password)
    //val hashed = BCrypt.with(SecureRandom()).hashToChar(10, dto.password.toCharArray());

    val userService by application.inject<UserService>()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()


    route("/auth", {
        tags("auth")
    }) {
        fun generateJwtPayload(jwt: JwtInfo): LoginAnswerDto {
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", jwt.email)
                .withClaim("role", jwt.role)
                .withExpiresAt(Date(System.currentTimeMillis() + 2 * 60 * 60000))
                .sign(Algorithm.HMAC256(secret))

            val refreshToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("refresh", true)
                .withClaim("email", jwt.email)
                .withClaim("username", jwt.username)
                .withClaim("role", jwt.role)
                .withClaim("id", jwt.userId)
                .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60000))
                .sign(Algorithm.HMAC256(secret))

            return LoginAnswerDto(
                email = jwt.email,
                name = jwt.username,
                jwt = JwtDto(
                    userId = jwt.userId,
                    token = token,
                    refresh = refreshToken
                )
            )
        }

        post("/login", {
            request {
                body<AuthDto>()
            }
        })
        {
            val dto = call.receive<AuthDto>()
            val user = userService.getByEmail(dto.email)

            if (user == null) {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respondText(
                    Json.encodeToString(hashMapOf("error" to "Wrong email")),
                    ContentType.Application.Json
                )
                return@post
            }

            val res = BCrypt.verifyer().verify(dto.password.toCharArray(), user.password)
            if (!res.verified) {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respondText(
                    Json.encodeToString(hashMapOf("error" to "Wrong password")),
                    ContentType.Application.Json
                )
                return@post
            }
            val jwt = JwtInfo(
                userId = user.id!!,
                email = user.email,
                username = user.name!!,
                role = user.role.toString()
            )
            call.respond(generateJwtPayload(jwt))

        }
        authenticate("userAuth") {
            get("/page") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respond(HttpStatusCode.OK, "Hi, $email! Token is expired at $expiresAt ms.")
            }
        }

        post("/refresh", {
            request {
                body<RefreshTokenDto>()
            }
        })
        {
            val refresh = call.receive<RefreshTokenDto>().token
            val verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("refresh", true)
                .build()

            val res = verifier.verify(refresh)

            val email = res.claims["email"]!!.asString()
            val username = res.claims["username"]!!.asString()
            val userId = res.claims["id"]!!.asLong()
            val role = res.claims["role"]!!.asString()

            val jwt = JwtInfo(userId, email, username, role)
            if (res != null) {
                call.respond(generateJwtPayload(jwt))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("register", {
            request {
                body<CreateUserDto>()
            }
        }) {
            val dto = call.receive<CreateUserDto>()
            dto.apply {
                this.password = String(BCrypt.with(SecureRandom()).hashToChar(10, this.password.toCharArray()));
            }
            val id = userService.createUser(dto)
            id?.let { call.respond(HttpStatusCode.Created, id) }
                ?: call.respond(HttpStatusCode.InternalServerError)
        }

    }
}

