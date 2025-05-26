package ru.modas.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Скачай postman и смотри ответ на http://0.0.0.0:8080/ в режиме GET
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
