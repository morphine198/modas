package ru.modas.database.account.sessions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("sessions") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_user = Model.integer("id_user")
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
                            id_user = it[Model.id_user],
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