package ru.modas.features.character.create.template

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.templates.DTO as templateDTO
import ru.modas.database.character.templates.Model as templateModel
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.utils.isValidEmail
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun createTemplate () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        // Вставка данных
        templateModel.insert(
            templateDTO(
                template_name = receive.template_name,
                id_sheet = null,
                description = receive.description,
            )
        )

        // Обратный отзыв
        call.respond(HttpStatusCode.OK, "Template created")
    }
}