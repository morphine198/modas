package ru.modas.features.create

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class Controller (private val call: ApplicationCall) {
    suspend fun createSheet () {
        val receive = call.receive<DataReceive>()

        val sessionKey = UUID.randomUUID().toString()
        val tokenDTO = ru.modas.database.account.sessions.Model.fetch(receive.token)
        if (tokenDTO != null) {
            val classDTO = ru.modas.database.sheet.definition.classes.Model.fetch(receive.class_name)
            if (classDTO == null) {
                // Вставка данных в таблицу "classes"
                ru.modas.database.sheet.definition.classes.Model.insert(
                    ru.modas.database.sheet.definition.classes.DRO(
                        class_name = receive.class_name,
                    )
                )
            }

            val originDTO = ru.modas.database.sheet.definition.origins.Model.fetch(receive.origin_name)
            if (originDTO == null) {
                // Вставка данных в таблицу "origins"
                ru.modas.database.sheet.definition.origins.Model.insert(
                    ru.modas.database.sheet.definition.origins.DRO(
                        origin_name = receive.origin_name,
                    )
                )
            }

            val raceDTO = ru.modas.database.sheet.definition.races.Model.fetch(receive.race_name)
            if (raceDTO == null) {
                // Вставка данных в таблицу "races"
                ru.modas.database.sheet.definition.races.Model.insert(
                    ru.modas.database.sheet.definition.races.DRO(
                        race_name = receive.race_name,
                    )
                )
            }

            val worldviewDTO = ru.modas.database.sheet.definition.worldviews.Model.fetch(receive.worldview)
            if (worldviewDTO == null) {
                // Вставка данных в таблицу "worldviews"
                ru.modas.database.sheet.definition.worldviews.Model.insert(
                    ru.modas.database.sheet.definition.worldviews.DRO(
                        worldview = receive.worldview,
                    )
                )
            }

            // Вставка данных в таблицу "characters"
            if (classDTO != null) {
                if (originDTO != null) {
                    if (raceDTO != null) {
                        if (worldviewDTO != null) {
                            ru.modas.database.sheet.appearance.characters.Model.insert(
                                ru.modas.database.sheet.appearance.characters.DRO(
                                    experience = receive.experience,
                                    key = sessionKey,
                                    id_class = classDTO.id_class,
                                    id_origin = originDTO.id_origin,
                                    id_race = raceDTO.id_race,
                                    id_worldview = worldviewDTO.id_worldview,
                                )
                            )
                        }
                    }
                }
            }

            val charactersDTO = ru.modas.database.sheet.appearance.characters.Model.fetch(sessionKey)
            if (charactersDTO != null) {
                // Вставка данных в таблицу "users_characters"
                ru.modas.database.sheet.appearance.users_characters.Model.insert(
                    ru.modas.database.sheet.appearance.users_characters.DTO(
                        id_user = tokenDTO.id_user,
                        id_character = charactersDTO.id_character,
                        character_name = receive.character_name,
                    )
                )
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Invalid 'characters' ID")
                return
            }

            return
        }

        call.respond(HttpStatusCode.Conflict, "Invalid token")
    }
}