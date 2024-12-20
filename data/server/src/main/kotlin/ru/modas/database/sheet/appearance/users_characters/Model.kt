package ru.modas.database.sheet.appearance.users_characters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users_characters") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_user = Model.integer("id_user")
    val id_character = Model.integer("id_character")
    val character_name = Model.varchar("character_name", 256)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[id_user] = dto.id_user
                it[id_character] = dto.id_character
                it[character_name] = dto.character_name
            }
        }
    }

    suspend fun fetch(id_user: Int): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_user eq id_user }.singleOrNull()
                    model?.let {
                        DTO(
                            id_user = it[Model.id_user],
                            id_character = it[Model.id_character],
                            character_name = it[Model.character_name],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}