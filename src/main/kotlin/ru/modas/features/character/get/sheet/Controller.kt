package ru.modas.features.character.get.sheet

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.sheets.Model as sheetModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun getSheet () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        val sheetDTO = sheetModel.fetch(receive.id_sheet)
        if (sheetDTO == null) {
            call.respond(HttpStatusCode.Conflict, "Sheet not found")
            return
        }

        // Обратный отзыв
        call.respond(DataResponse(
            template_name = sheetDTO.template_name
        ))
    }
}