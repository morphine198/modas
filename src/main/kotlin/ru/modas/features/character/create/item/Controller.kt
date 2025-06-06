package ru.modas.features.character.create.item

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.items.DTO as itemDTO
import ru.modas.database.character.items.Model as itemModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun createItem () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        // Вставка данных
        val id_item = itemModel.insert(
            itemDTO(
                id_sheet = receive.id_sheet,
                rank = receive.rank,
                collection = receive.collection,
                type = receive.type,
                value = receive.value,
                description = receive.description,
            )
        )

        // Возврат id таблицы items
        call.respond(DataResponse(
            id_item = id_item,
        ))
    }
}