package ru.modas.features.character.get.template

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetTemplateRouting() {
    routing {
        post("/get/template") {
            Controller(call).getTemplate()
        }
    }
}