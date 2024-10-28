package ru.modas.database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("users") {
    private val id_user = Model.integer("id_user")
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

    fun fetch(id_user: Int): DTO {
        val model = Model.selectAll().where { Model.id_user.eq(id_user) }.single()
        return DTO(
            id_user = model[Model.id_user],
            login = model[Model.login],
            password = model[Model.password],
            email = model[Model.email],
        )
    }
}