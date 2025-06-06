package ru.modas.features.character.create.item

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCreateItemRouting() {
    routing {
        post("/create/item") {
            Controller(call).createItem()
        }
    }
}