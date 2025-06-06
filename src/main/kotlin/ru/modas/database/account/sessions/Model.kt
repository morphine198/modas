package ru.modas.database.account.sessions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.account.users.Model as userModel

object Model: Table("sessions") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val token = Model.varchar("token", 64)
    val time = Model.timestamp("time")
    val login = Model.reference("login", userModel.login)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[token] = dto.token
                it[login] = dto.login
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
                            token = it[Model.token],
                            time = it[Model.time],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchByToken(token: String): DRO_T? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.token eq token }.singleOrNull()
                    model?.let {
                        DRO_T(
                            login = it[Model.login],
                            time = it[Model.time],
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