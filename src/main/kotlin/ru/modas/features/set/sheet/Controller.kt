package ru.modas.features.set.sheet

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.Int
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.database.character.templates.DTO as templatesDTO
import ru.modas.database.character.templates.Model as templatesModel
import ru.modas.database.character.template_panels.DTO as templateDTO
import ru.modas.database.character.template_panels.Model as templateModel
import ru.modas.database.character.panel_items.DTO as panelDTO
import ru.modas.database.character.panel_items.Model as panelModel
import ru.modas.database.character.items.DTO as itemDTO
import ru.modas.database.character.items.Model as itemModel

class Controller (private val call: ApplicationCall) {
    // неэффективный поиск, нужно будет улучшить
    suspend fun setSheet () {
        val receive = call.receive<DataReceive>()

        val sessionDRO = sessionModel.fetch(receive.token)
        if (sessionDRO == null) {
            call.respond(HttpStatusCode.Conflict, "Invalid token")
        }

        val templateType = templatesModel.fetch(receive.id_template)?.type
        var templateID = receive.id_template

        if (templateType == "structure") {
            templatesModel.insert(
                templatesDTO(
                    type = "fillable",
                    name = receive.name,
                )
            )
            val newTemplate = templatesModel.fetch(receive.name)
            if (newTemplate != null) {
                templateID = newTemplate.id_template
            } else {
                call.respond(HttpStatusCode.OK, "Template iteration not found")
            }
        } else {
            call.respond(HttpStatusCode.OK, "Template not found")
        }


        for ((panelRank, itemMap) in receive.sheet) {
            var panelCount = 1

            for ((itemRank, itemList) in itemMap) {
                var panelCount = 1

                panelModel.insert(
                    panelDTO(
                        rank = itemRank,
                        id_panel = panelCount,
                        id_item = 1 // затычка
                    )
                )
            }

            templateModel.insert(
                templateDTO(
                    rank = panelRank,
                    id_panel = panelCount++,
                    id_template = templateID,
                )
            )
        }

        // Обратный ответ
        call.respond(HttpStatusCode.OK, "Character sheet sent")
    }
}