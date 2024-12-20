package ru.modas.database.sheet.force.stats

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("stats") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val note = Model.varchar("note", 1024)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[note] = dto.note
            }
        }
    }

    suspend fun fetch(note: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.note eq note }.singleOrNull()
                    model?.let {
                        DTO(
                            note = it[Model.note],
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