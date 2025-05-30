package ru.modas.features.set.characters

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.database.character.user_characters.DTO as characterDTO
import ru.modas.database.character.user_characters.Model as characterModel
import ru.modas.utils.isValidEmail
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun setCharacter () {
        val receive = call.receive<DataReceive>()

        val sessionDRO = sessionModel.fetch(receive.token)
        if (sessionDRO == null) {
            call.respond(HttpStatusCode.Conflict, "Invalid token")
        }

        characterModel.insert(
            characterDTO(
                name = receive.name,
                login = receive.login,
                id_template = receive.id_template,
            )
        )

        // Обратный ответ
        call.respond(HttpStatusCode.OK, "Character info sent")
    }
}