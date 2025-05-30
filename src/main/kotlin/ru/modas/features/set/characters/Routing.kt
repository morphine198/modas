package ru.modas.features.set.characters

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSetCharactersRouting() {
    routing {
        post("/set/characters") {
            Controller(call).setCharacter()
        }
    }
}