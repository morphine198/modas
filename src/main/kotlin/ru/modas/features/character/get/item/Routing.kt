package ru.modas.features.character.get.item

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetItemRouting() {
    routing {
        post("/get/item") {
            Controller(call).getItem()
        }
    }
}