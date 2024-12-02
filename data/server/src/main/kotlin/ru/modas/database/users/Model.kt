package ru.modas.database.users

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users") {
    val login = Model.varchar("login", 64)
    val password = Model.varchar("password", 256)
    val email = Model.varchar("email", 128)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[login] = dto.login
                it[password] = dto.password
                it[email] = dto.email
            }
        }
    }

    suspend fun fetch(login: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.login eq login }.singleOrNull()
                    model?.let {
                        DTO(
                            login = it[Model.login],
                            password = it[Model.password],
                            email = it[Model.email],
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