package ru.modas.features.account.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.account.sessions.DTO as sessionDTO
import ru.modas.database.account.sessions.Model as sessionModel
import ru.modas.database.character.templates.Model as userModel
import ru.modas.features.account.register.DataResponse
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun loginUser () {
        val receive = call.receive<DataReceive>()

        val userDRO = userModel.fetch(receive.login)
        if (userDRO != null) {
            val token = UUID.randomUUID().toString()

            // Отправка токена
            call.respond(DataResponse(token = token))
            // Вставка токена
            sessionModel.insert(
                sessionDTO(
                    login = receive.login,
                    token = token,
                )
            )
            return
        }

        call.respond(HttpStatusCode.Conflict, "User not found")
    }
}