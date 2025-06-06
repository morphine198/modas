package ru.modas.features.character.update.template

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUpdateTemplateRouting() {
    routing {
        post("/update/template") {
            Controller(call).updateTemplate()
        }
    }
}