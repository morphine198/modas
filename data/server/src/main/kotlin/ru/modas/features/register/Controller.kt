package ru.modas.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.users.DTO
import ru.modas.database.users.Model
import ru.modas.utils.isValidEmail
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun registerUser () {
        val receive = call.receive<DataReceive>()

        // 'Проверка' валидности email
        if (!receive.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            return
        }

        // Проверка повтора login
        val userDTO = Model.fetch(receive.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
            return
        }

        // Вставка данных нового пользователя
        Model.insert(
            DTO(
                login = receive.login,
                password = receive.password,
                email = receive.email,
            )
        )

        // Отправка токена
        call.respond(DataResponse(token = UUID.randomUUID().toString()))
    }
}