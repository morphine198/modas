package ru.modas.database.character.items

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("items") {
    val id_item = Model.integer("id_item")
    val type = Model.varchar("type", 128)
    val value = Model.varchar("value", 3072)
    val description = Model.varchar("description", 128)

    override val primaryKey = PrimaryKey(Model.id_item)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[type] = dto.type
                it[value] = dto.value
                it[description] = dto.description
            }
        }
    }

    suspend fun fetch(id_item: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_item eq id_item }.singleOrNull()
                    model?.let {
                        DRO(
                            type = it[Model.type],
                            value = it[Model.value],
                            description = it[Model.description],
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