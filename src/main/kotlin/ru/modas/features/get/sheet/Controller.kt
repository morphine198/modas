package ru.modas.features.get.sheet

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.template_panels.Model as templateModel
import ru.modas.database.character.panel_items.Model as panelModel
import ru.modas.database.character.items.Model as itemModel
import ru.modas.utils.isValidEmail
import java.util.*

class Controller (private val call: ApplicationCall) {
    // неэффективный поиск, нужно будет улучшить
    suspend fun getSheet () {
        val receive = call.receive<DataReceive>()

        val sheet: MutableMap<Int, MutableMap<Int, MutableList<String>>> = mutableMapOf()
        var itemRelation: MutableMap<Int, MutableList<String>> = mutableMapOf()

        // получает все ранги и id панелей
        val panelDRO = templateModel.fetchAll(receive.id_template)
        if (panelDRO != null) {
            for (currentPanelDRO in panelDRO) {

                // получает все ранги и id вещей
                val itemDRO = panelModel.fetchAll(currentPanelDRO.id_panel)
                if (itemDRO != null) {
                    for (currentItemDRO in itemDRO) {

                        val items = itemModel.fetch(currentItemDRO.id_item)
                        if (items != null) {
                            itemRelation.put(
                                currentItemDRO.rank, mutableListOf(
                                    items.type,
                                    items.value,
                                    items.description,
                                )
                            )
                        }

                    }
                }

                sheet.put(currentPanelDRO.rank, itemRelation)
                itemRelation = mutableMapOf()
            }
        }

        // Отправка
        call.respond(DataResponse(
            sheet = sheet,
        ))
    }
}