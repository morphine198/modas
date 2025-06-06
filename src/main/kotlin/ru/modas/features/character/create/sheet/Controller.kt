package ru.modas.features.character.create.sheet

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.sheets.DTO as sheetDTO
import ru.modas.database.character.sheets.Model as sheetModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun createSheet () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        // Вставка данных
        val id_sheet = sheetModel.insert(
            sheetDTO(
                template_name = receive.template_name,
            )
        )

        // Возврат id таблицы sheets
        call.respond(DataResponse(
            id_sheet = id_sheet,
        ))
    }
}