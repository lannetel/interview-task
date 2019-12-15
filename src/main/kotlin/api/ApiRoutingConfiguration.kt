package api

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import persist.JdbcConfiguration
import persist.UserService
import java.time.LocalDate

fun Application.configureRouting() {
    val userService = UserService(JdbcConfiguration())

    routing {
        get("interview-task/count-user/{birthdate}") {
            runCatching {
                log.info(call.request.uri)
                val birthdate = LocalDate.parse(call.parameters["birthdate"].orEmpty())
                userService.countUserByBirthdate(birthdate)
            }.map {
                call.respond(HttpStatusCode.OK, "Number of users born this year = $it")
            }.getOrElse {
                call.respond(HttpStatusCode.InternalServerError, it.message.orEmpty())
            }
        }
    }
}
