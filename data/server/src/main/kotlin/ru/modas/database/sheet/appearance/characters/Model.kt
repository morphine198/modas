package ru.modas.database.sheet.appearance.characters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("characters") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_character = Model.integer("id_character")
    val experience = Model.integer("experience")
    val key = Model.varchar("key", 40)
    val id_class = Model.integer("id_class")
    val id_origin = Model.integer("id_origin")
    val id_race = Model.integer("id_race")
    val id_worldview = Model.integer("id_worldview")
    //val id_hit = Model.integer("id_hit")

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[experience] = dro.experience
                it[key] = dro.key
            }
        }
    }

    suspend fun fetch(key: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.key eq key }.singleOrNull()
                    model?.let {
                        DTO(
                            id_character = it[Model.id_character],
                            experience = it[Model.experience],
                            key = it[Model.key],
                            id_class = it[Model.id_class],
                            id_origin = it[Model.id_origin],
                            id_race = it[Model.id_race],
                            id_worldview = it[Model.id_worldview],
                            //id_hit = it[Model.id_hit],
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