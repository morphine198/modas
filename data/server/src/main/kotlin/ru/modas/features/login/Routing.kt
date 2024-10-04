package ru.modas.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.modas.cache.*
import java.util.UUID

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val receive = call.receive<DataReceive>()
            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                val token = UUID.randomUUID().toString()
                InMemoryCache.token.add(TokenCache(
                    login = receive.login,
                    token = token
                ))
                call.respond(DataResponse(token = token))
                return@post
            }

            call.respond(HttpStatusCode.BadRequest)
        }
    }
}