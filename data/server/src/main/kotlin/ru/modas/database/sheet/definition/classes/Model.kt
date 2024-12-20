package ru.modas.database.sheet.definition.classes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("classes") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы

    val id_class = Model.integer("id_class")
    val class_name = Model.varchar("class_name", 256)

    fun insert(dro: DRO) {
        transaction {
            Model.insert {
                it[class_name] = dro.class_name
            }
        }
    }

    suspend fun fetch(class_name: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.class_name eq class_name }.singleOrNull()
                    model?.let {
                        DTO(
                            id_class = it[Model.id_class],
                            class_name = it[Model.class_name],
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