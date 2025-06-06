package ru.modas.features.character.update.item

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.character.items.DRO as itemDRO
import ru.modas.database.character.items.Model as itemModel
import ru.modas.database.account.sessions.Model as sessionModel

class Controller (private val call: ApplicationCall) {
    suspend fun updateItem () {
        val receive = call.receive<DataReceive>()

        // Проверка на валидность токена
        if (sessionModel.fetchByToken(receive.token) == null) {
            call.respond(HttpStatusCode.Conflict, "Token not found")
            return
        }

        // Вставка данных нового пользователя
        itemModel.update(
            id_sheet = receive.id_sheet,
            id_item = receive.id_item,
            dro = itemDRO(
                rank = receive.rank,
                collection = receive.collection,
                type = receive.type,
                value = receive.value,
                description = receive.description,
            )
        )

        // Обратный отзыв
        call.respond(HttpStatusCode.OK, "Item updated")
    }
}