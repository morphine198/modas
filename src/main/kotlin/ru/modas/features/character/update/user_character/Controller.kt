package ru.modas.features.character.update.user_character

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.user_characters.DTO as characterDTO
import ru.modas.database.character.user_characters.Model as characterModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun updateCharacter () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        // Вставка данных нового пользователя
        characterModel.update(
            characterDTO(
                login = receive.login,
                id_sheet = receive.id_sheet,
                character_name = receive.character_name,
            )
        )

        // Обратный отзыв
        call.respond(HttpStatusCode.OK, "Character updated")
    }
}