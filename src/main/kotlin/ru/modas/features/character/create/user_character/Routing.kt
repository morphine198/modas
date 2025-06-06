package ru.modas.features.character.create.user_character

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCreateCharacterRouting() {
    routing {
        post("/create/character") {
            Controller(call).createCharacter()
        }
    }
}