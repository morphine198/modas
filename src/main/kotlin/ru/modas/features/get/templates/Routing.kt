package ru.modas.features.get.templates

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetTemplatesRouting() {
    routing {
        post("/get/templates") {
            Controller(call).getTemplates()
        }
    }
}