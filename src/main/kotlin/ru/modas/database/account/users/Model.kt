package ru.modas.database.account.users

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val login = Model.varchar("login", 64)
    val email = Model.varchar("email", 128)
    val password = Model.varchar("password", 256)

    override val primaryKey = PrimaryKey(login)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[login] = dto.login
                it[email] = dto.email
                it[password] = dto.password
            }
        }
    }

    suspend fun fetch(login: String): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.login eq login }.singleOrNull()
                    model?.let {
                        DRO(
                            login = it[Model.login],
                            email = it[email],
                            password = it[password],
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