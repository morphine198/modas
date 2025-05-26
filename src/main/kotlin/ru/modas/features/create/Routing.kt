package ru.modas.features.create

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCreateSheetRouting() {
    routing {
        post("/create") {
            Controller(call).createSheet()
        }
    }
}