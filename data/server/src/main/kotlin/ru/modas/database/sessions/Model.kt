package ru.modas.database.sessions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("sessions") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val token = Model.varchar("token", 40)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[token] = dto.token
            }
        }
    }

    suspend fun fetch(token: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.token eq token }.singleOrNull()
                    model?.let {
                        DTO(
                            token = it[Model.token],
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