package ru.modas.features.account.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.modas.database.account.users.DTO as userDTO
import ru.modas.database.account.users.Model as userModel
import ru.modas.database.account.sessions.DTO as sessionDTO
import ru.modas.database.account.sessions.Model as sessionModel
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
        val userDTO = userModel.fetch(receive.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
            return
        }

        // Вставка данных нового пользователя
        userModel.insert(
            userDTO(
                login = receive.login,
                password = receive.password,
                email = receive.email,
            )
        )

        // Отправка токена
        call.respond(DataResponse(token = UUID.randomUUID().toString()))
        // Вставка токена
        sessionModel.insert(
            sessionDTO(
                token = UUID.randomUUID().toString(),
                login = receive.login,
            )
        )
    }
}