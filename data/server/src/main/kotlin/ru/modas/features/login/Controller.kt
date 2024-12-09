package ru.modas.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.users.DTO
import ru.modas.features.register.DataResponse
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun loginUser () {
        val receive = call.receive<DataReceive>()

        val userDTO = ru.modas.database.users.Model.fetch(receive.login)
        if (userDTO != null) {
            val token = UUID.randomUUID().toString()

            // Отправка токена
            call.respond(DataResponse(token = token))

            // Вставка токена
            ru.modas.database.sessions.Model.insert(
                ru.modas.database.sessions.DTO(
                    token = token,
                )
            )
            return
        }

        call.respond(HttpStatusCode.Conflict, "User not found")
    }
}