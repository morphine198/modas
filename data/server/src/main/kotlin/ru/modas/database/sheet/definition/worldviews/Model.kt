package ru.modas.database.sheet.definition.worldviews

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("worldviews") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_worldview = Model.integer("id_worldview")
    val worldview = Model.varchar("worldview", 256)

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[worldview] = dro.worldview
            }
        }
    }

    suspend fun fetch(worldview: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.worldview eq worldview }.singleOrNull()
                    model?.let {
                        DTO(
                            id_worldview = it[Model.id_worldview],
                            worldview = it[Model.worldview],
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