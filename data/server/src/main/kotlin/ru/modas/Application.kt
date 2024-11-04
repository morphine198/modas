package ru.modas

import ru.modas.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import ru.modas.features.login.configureLoginRouting
import ru.modas.features.register.configureRegisterRouting
import java.io.File

fun main() {

    // Для этого всего нужно сделать свой конфиг файл
    /*print("Port: ")
    val port = readLine() // 8080
    print("Host: ")
    val host = readLine() // "0.0.0.0"
    print("Directory for .mds: ")
    val pwd = readLine() //"<path>/mds"*/

    val pwd = "/home/moilenke/mds"

    val url = File(pwd, "url.mds").readText()
    val usr = File(pwd, "usr.mds").readText()
    val psswd = File(pwd, "psswd.mds").readText()

    Database.connect(
        url,
        driver = "org.postgresql.Driver",
        user = usr,
        password = psswd
    )

    // port и host нужно поменять на вводимые значения
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
}
