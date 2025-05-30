package ru.modas.features.get.characters

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.user_characters.Model as characterModel
import ru.modas.utils.isValidEmail
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun getCharacters() {
        val receive = call.receive<DataReceive>()
        val characterDRO = characterModel.fetchAll(receive.login)

        val character: MutableMap<Int, String> = mutableMapOf()
        if (characterDRO != null) {
            for (DRO in characterDRO) {
                character.put(DRO.id_template, DRO.name)
            }
        } else {
            call.respond(HttpStatusCode.Conflict, "Characters list not found")
        }

        // Отправка
        call.respond(DataResponse(
            character = character,
        ))
    }
}