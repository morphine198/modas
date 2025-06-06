package ru.modas.features.account.register

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            Controller(call).registerUser()
        }
    }
}