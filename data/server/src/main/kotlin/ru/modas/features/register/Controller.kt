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
        val userDTO = Model.fetch(receive.login)

        if (!receive.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            //val token = UUID.randomUUID().toString()

            Model.insert(
                DTO(
                    login = receive.login,
                    password = receive.password,
                    email = receive.email,
                )
            )

            /*InMemoryCache.token.add(
                TokenCache(
                    login = receive.login,
                    token = token
                )
            )*/

            call.respond(DataResponse(token = UUID.randomUUID().toString()))
        }
    }
}