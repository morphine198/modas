package ru.modas.features.get.templates

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.database.character.templates.Model as templateModel
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun getTemplates() {
        val receive = call.receive<DataReceive>()

        val sessionDRO = sessionModel.fetch(receive.token)
        if (sessionDRO == null) {
            call.respond(HttpStatusCode.Conflict, "Invalid token")
        }

        val templateDRO = templateModel.fetchAll(receive.type)
        val templates: MutableMap<Int, String> = mutableMapOf()
        if (templateDRO != null) {
            for (DRO in templateDRO) {
                templates.put(DRO.id_template, DRO.name)
            }
        } else {
            call.respond(HttpStatusCode.Conflict, "Template list not found")
        }

        // Отправка
        call.respond(DataResponse(
            templates = templates,
        ))
    }
}