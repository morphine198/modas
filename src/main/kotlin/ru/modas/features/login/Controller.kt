package ru.modas.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.account.sessions.DTO as sessionDTO
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.database.account.users.Model as userModel
import ru.modas.features.register.DataResponse
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun loginUser () {
        val receive = call.receive<DataReceive>()

        val userDTO = userModel.fetch(receive.login)
        if (userDTO != null) {
            val token = UUID.randomUUID().toString()

            // Отправка токена
            call.respond(DataResponse(token = token))
            // Вставка токена
            sessionModel.insert(
                sessionDTO(
                    login = userDTO.login,
                    token = token,
                )
            )
            return
        }

        call.respond(HttpStatusCode.Conflict, "User not found")
    }
}