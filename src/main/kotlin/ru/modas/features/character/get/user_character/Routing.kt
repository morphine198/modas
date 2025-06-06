package ru.modas.features.character.get.user_character

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureGetCharacterRouting() {
    routing {
        post("/get/character") {
            Controller(call).getCharacter()
        }
    }
}