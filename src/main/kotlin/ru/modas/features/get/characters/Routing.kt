package ru.modas.features.get.characters

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetCharactersRouting() {
    routing {
        post("/get/characters") {
            Controller(call).getCharacters()
        }
    }
}