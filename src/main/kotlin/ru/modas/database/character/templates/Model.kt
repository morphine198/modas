package ru.modas.database.character.templates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("templates") {
    val id_template = Model.integer("id_template")
    val type = Model.varchar("type", 128)
    val name = Model.varchar("name", 256)

    override val primaryKey = PrimaryKey(id_template)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[type] = dto.type
                it[name] = dto.name
            }
        }
    }

    suspend fun fetch(id_template: Int): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_template eq id_template }.singleOrNull()
                    model?.let {
                        DTO(
                            type = it[Model.type],
                            name = it[Model.name],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetch(name: String): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.name eq name }.singleOrNull()
                    model?.let {
                        DRO(
                            id_template = it[Model.id_template],
                            name = it[Model.name],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchAll(type: String): List<DRO>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.type eq type }
                        .map {
                            DRO(
                                id_template = it[Model.id_template],
                                name = it[Model.name],
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