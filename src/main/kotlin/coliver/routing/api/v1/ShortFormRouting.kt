package coliver.routing.api.v1

import coliver.dto.CreatePropertiesDto
import coliver.dto.form.AgeFilterDto
import coliver.dto.form.CreateFormDto
import coliver.dto.form.FilterDto
import coliver.dto.form.PriceFilterDto
import coliver.services.ShortFormService
import io.github.smiley4.ktorswaggerui.dsl.routing.delete
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.shortFormRouting() {

    val shortFormService by application.inject<ShortFormService>()

    route("/form", {
        tags("shortForms")
    }) {

        get("/getShortForms") {
            call.respond(shortFormService.getAll())
        }

        get("/getShortFormById/{id}", {
            request {
                queryParameter<Long>("id")
            }
        }) {
            val id = call.parameters["id"]!!.toLong()
            call.respond(shortFormService.getById(id))
        }

        get("/getShortFormsForUserId/{userId}", {
            request {
                queryParameter<Long>("userId")
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            call.respond(shortFormService.getForUser(userId))
        }

        post("/getShortFormsWithFilter/{userId}", {
            request {
                queryParameter<Long>("userId")
                body<FilterDto> {
                    example("Example filter") {
                        value = FilterDto(
                            price = PriceFilterDto(0L, 100000L),
                            age = AgeFilterDto(1, 25),
                            cityId = listOf(),
                            metroIds = listOf(),
                            smoking = false,
                            alcohol = true,
                            isClean = true,
                            petFriendly = false,
                        )
                    }
                }
            }
        }) {
            val userId = call.parameters["userId"]!!.toLong()
            val filter = call.receive<FilterDto>()
            call.respond(shortFormService.getWithFilter(userId, filter))
        }

        post("/getWithFilterWithoutId", {
            request {
                body<FilterDto> {
                    example("Example filter") {
                        value = FilterDto(
                            price = PriceFilterDto(0L, 100000L),
                            age = AgeFilterDto(1, 25),
                            cityId = listOf(),
                            metroIds = listOf(),
                            smoking = false,
                            alcohol = true,
                            isClean = true,
                            petFriendly = false,
                        )
                    }
                }
            }
        }) {
            val filter = call.receive<FilterDto>()
            call.respond(shortFormService.getWithFilterWithoutId(filter))
        }

        post("/createFormForUserById", {
            request {
                body<CreateFormDto> {
                    example("Example CreateForm") {
                        value = CreateFormDto(
                            userId = 6,
                            description = "Описание",
                            homeTypesIds = listOf(1L, 2L),
                            rating = 5.0,
                            reviews = listOf("Yes"),
                            photoId = 1,
                            properties = CreatePropertiesDto(
                                smoking = false,
                                alcohol = false,
                                isClean = false,
                                petFriendly = false,
                                homeOwnerId = null,
                            ),
                            cityId = 1,
                            metroIds = listOf(1,2),
                            budget = 50000L,
                            dateMove = java.time.LocalDateTime.now(),
                            onlineDateTime = java.time.LocalDateTime.now()
                        )
                    }
                }
            }
        }) {
            val dto = call.receive<CreateFormDto>()
            call.respond(HttpStatusCode.Created, shortFormService.createForm(dto))
        }

        delete("/remove/{id}", {
            request {
                queryParameter<Long>("formId")
            }
        }) {
            val formId = call.parameters["formId"]!!.toLong()
            call.respond(HttpStatusCode.OK, shortFormService.deleteForm(formId))
        }
    }
}
