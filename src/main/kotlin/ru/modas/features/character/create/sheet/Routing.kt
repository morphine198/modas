package ru.modas.features.character.create.sheet

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCreateSheetRouting() {
    routing {
        post("/create/sheet") {
            Controller(call).createSheet()
        }
    }
}