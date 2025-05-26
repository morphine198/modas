package ru.modas.database.sheet.action.hits

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("hits") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val max = Model.integer("max")
    val current = Model.integer("current")
    val temporary = Model.integer("temporary")

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[max] = dto.max
                it[current] = dto.current
                it[temporary] = dto.temporary
            }
        }
    }

    suspend fun fetch(max: Int): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.max eq max }.singleOrNull()
                    model?.let {
                        DTO(
                            max = it[Model.max],
                            current = it[Model.current],
                            temporary = it[Model.temporary],
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