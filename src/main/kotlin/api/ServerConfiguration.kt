package api

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import java.text.SimpleDateFormat

fun Application.configureServer() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT)
            configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            registerModule(JavaTimeModule())
            dateFormat = SimpleDateFormat("yyyy-MM-dd")
        }
        configureRouting()
    }
}