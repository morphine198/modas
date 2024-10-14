package ru.modas.database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users") {
    private val login = Model.varchar("login", 32)
    private val password = Model.varchar("password", 256)
    private val email = Model.varchar("email", 128)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[login] = dto.login
                it[password] = dto.password
                it[email] = dto.email
            }
        }
    }

    fun fetch(login: String): DTO {
        val model = Model.selectAll().where { Model.login.eq(login) }.single()
        return DTO(
            login = model[Model.login],
            password = model[Model.password],
            email = model[Model.email],
        )
    }
}