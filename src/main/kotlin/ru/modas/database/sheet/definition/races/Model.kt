package ru.modas.database.sheet.definition.races

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("races") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_race = Model.integer("id_race")
    val race_name = Model.varchar("race_name", 256)

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[race_name] = dro.race_name
            }
        }
    }

    suspend fun fetch(race_name: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.race_name eq race_name }.singleOrNull()
                    model?.let {
                        DTO(
                            id_race = it[Model.id_race],
                            race_name = it[Model.race_name],
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