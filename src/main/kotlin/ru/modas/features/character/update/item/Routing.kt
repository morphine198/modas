package ru.modas.features.character.update.item

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUpdateItemRouting() {
    routing {
        post("/update/item") {
            Controller(call).updateItem()
        }
    }
}