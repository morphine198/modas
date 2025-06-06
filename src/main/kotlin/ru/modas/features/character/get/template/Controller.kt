package ru.modas.features.character.get.template

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.templates.Model as templateModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun getTemplate () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        val DTOList: MutableMap<String, MutableMap<Int?, String?>> = mutableMapOf()

        val templateList = templateModel.fetch()
        if (templateList != null) {
            for (templateDTO in templateList) {
                DTOList.put(
                    templateDTO.template_name,
                    value = mutableMapOf(templateDTO.id_sheet to templateDTO.description)
                )
            }
        }

        // Обратный отзыв
        call.respond(DataResponse(
            DTOList = DTOList
        ))
    }
}