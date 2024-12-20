package ru.modas.database.sheet.definition.origins

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("origins") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_origin = Model.integer("id_origin")
    val origin_name = Model.varchar("origin_name", 256)

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[origin_name] = dro.origin_name
            }
        }
    }

    suspend fun fetch(origin_name: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.origin_name eq origin_name }.singleOrNull()
                    model?.let {
                        DTO(
                            id_origin = it[Model.id_origin],
                            origin_name = it[Model.origin_name],
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