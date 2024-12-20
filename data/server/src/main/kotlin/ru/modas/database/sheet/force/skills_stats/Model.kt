package ru.modas.database.sheet.force.skills_stats

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("skills_stats") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_skill = Model.integer("id_skill")
    val id_stat = Model.integer("id_stat")
    val skill_mod = Model.integer("skill_mod")

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[id_skill] = dto.id_skill
                it[id_stat] = dto.id_stat
                it[skill_mod] = dto.skill_mod
            }
        }
    }

    suspend fun fetch(id_skill: Int): DTO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_skill eq id_skill }.singleOrNull()
                    model?.let {
                        DTO(
                            id_skill = it[Model.id_skill],
                            id_stat = it[Model.id_stat],
                            skill_mod = it[Model.skill_mod],
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