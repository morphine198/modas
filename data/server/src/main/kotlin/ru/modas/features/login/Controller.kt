package ru.modas.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.users.Model
import ru.modas.features.register.DataResponse
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun loginUser () {
        val receive = call.receive<DataReceive>()

        val userDTO = Model.fetch(receive.login)
        if (userDTO != null) {
            // Отправка токена
            call.respond(DataResponse(token = UUID.randomUUID().toString()))
            return
        }

        call.respond(HttpStatusCode.Conflict, "User not found")
    }
}