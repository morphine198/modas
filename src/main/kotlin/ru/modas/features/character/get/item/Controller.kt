package ru.modas.features.character.get.item

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.items.Model as itemModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun getItem () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        val DTOList: MutableMap<Int, MutableList<*>> = mutableMapOf()

        val itemList = itemModel.fetch(receive.id_sheet)
        if (itemList != null) {
            for (itemDTO in itemList) {
                DTOList.put(
                    itemDTO.id_item,
                    value = mutableListOf(
                        itemDTO.rank,
                        itemDTO.collection,
                        itemDTO.type,
                        itemDTO.value,
                        itemDTO.description,
                    )
                )
            }
        }

        // Обратный отзыв
        call.respond(DataResponse(
            DTOList = DTOList
        ))
    }
}