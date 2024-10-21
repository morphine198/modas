package ru.modas.database.server.options

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("server_options") {
    private val ip = Model.varchar("ip", 20)
    private val port = Model.integer("port")

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[ip] = dto.ip
                it[port] = dto.port
            }
        }
    }

    fun fetch(login: String): DTO {
        val model = Model.selectAll().where { Model.ip.eq(ip) }.single()
        return DTO(
            ip = model[Model.ip],
            port = model[Model.port],
        )
    }
}