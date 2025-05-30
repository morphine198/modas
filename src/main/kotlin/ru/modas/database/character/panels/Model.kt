package ru.modas.database.character.panels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Model: Table("panels") {
    // Лучше в public не переводить, так как, возможно, это ломает fetch запросы
    val id_panel = Model.integer("id_panel")
    val name = Model.varchar("name", 256)

    override val primaryKey = PrimaryKey(id_panel)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[name] = dto.name
            }
        }
    }

    suspend fun fetch(id_panel: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_panel eq id_panel }.singleOrNull()
                    model?.let {
                        DRO(
                            id_panel = it[Model.id_panel],
                            name = it[name],
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