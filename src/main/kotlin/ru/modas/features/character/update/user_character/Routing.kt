package ru.modas.features.character.update.user_character

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUpdateCharacterRouting() {
    routing {
        post("/update/character") {
            Controller(call).updateCharacter()
        }
    }
}