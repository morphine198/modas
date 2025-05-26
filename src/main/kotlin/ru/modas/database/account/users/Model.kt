package ru.modas.database.account.users

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_user = Model.integer("id_user")
    val login = Model.varchar("login", 64)
    val password = Model.varchar("password", 256)
    val email = Model.varchar("email", 128)

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[login] = dro.login
                it[password] = dro.password
                it[email] = dro.email
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
                            id_user = it[Model.id_user],
                            login = it[Model.login],
                            password = it[password],
                            email = it[email],
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