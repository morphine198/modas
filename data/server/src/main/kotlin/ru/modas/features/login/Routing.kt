package ru.modas.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.modas.cache.*
import java.util.UUID

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            Controller(call).loginUser()
        }
    }
}