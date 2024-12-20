package ru.modas.database.sheet.force.skills

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("skills") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val skill_name = Model.varchar("skill_name", 512)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[skill_name] = dto.skill_name
            }
        }
    }

    suspend fun fetch(skill_name: String): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.skill_name eq skill_name }.singleOrNull()
                    model?.let {
                        DTO(
                            skill_name = it[Model.skill_name],
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