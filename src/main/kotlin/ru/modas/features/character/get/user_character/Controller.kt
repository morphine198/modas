package ru.modas.features.character.get.user_character

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.user_characters.Model as characterModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun getCharacter () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        val DTOList: MutableList<MutableMap<Int, String>> = mutableListOf()

        val characterList = characterModel.fetch(receive.login)
        if (characterList != null) {
            for (characterDTO in characterList) {
                DTOList.add(mutableMapOf(characterDTO.id_sheet to characterDTO.character_name))
            }
        }

        // Обратный отзыв
        call.respond(DataResponse(
            DTOList = DTOList
        ))
    }
}