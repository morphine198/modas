package ru.modas.features.character.create.template

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCreateTemplateRouting() {
    routing {
        post("/create/template") {
            Controller(call).createTemplate()
        }
    }
}