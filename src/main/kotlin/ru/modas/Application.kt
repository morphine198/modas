package ru.modas

import ru.modas.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import ru.modas.features.get.characters.configureGetCharactersRouting
import ru.modas.features.get.sheet.configureGetSheetRouting
import ru.modas.features.get.templates.configureGetTemplatesRouting
import ru.modas.features.login.configureLoginRouting
import ru.modas.features.register.configureRegisterRouting
import ru.modas.features.set.characters.configureSetCharactersRouting
import ru.modas.utils.ResourceReader
import java.io.File
import java.io.InputStream

fun main() {

    // Можно запихнуть в отдельный класс/функцию ------------------------------------------------------------>
    val inputStream: InputStream = ResourceReader().readTextResource("config.txt").byteInputStream()
    val lineList = mutableListOf<String>()

    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    lineList.forEach{it.trim()}
    // ------------------------------------------------------------------------------------------------------>

    // Это нужно сделать чуть поумнее -------------------------->
    val pwd = lineList[0]

    val url = (File(pwd, "url.mds").readText()).trim()
    val usr = (File(pwd, "usr.mds").readText()).trim()
    val psswd = (File(pwd, "psswd.mds").readText()).trim()
    // --------------------------------------------------------->

    Database.connect(
        url,
        driver = "org.postgresql.Driver",
        user = usr,
        password = psswd
    )

    embeddedServer(CIO, port = lineList[1].toInt(), host = lineList[2], module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
    configureGetCharactersRouting()
    configureGetSheetRouting()
    configureGetTemplatesRouting()
    configureSetCharactersRouting()
}
