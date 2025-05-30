package ru.modas.features.set.sheet

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSetSheetRouting() {
    routing {
        post("/set/sheet") {
            Controller(call).setSheet()
        }
    }
}