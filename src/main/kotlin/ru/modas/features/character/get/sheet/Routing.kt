package ru.modas.features.character.get.sheet

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetSheetRouting() {
    routing {
        post("/get/sheet") {
            Controller(call).getSheet()
        }
    }
}