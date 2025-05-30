package ru.modas.database.character.panel_items

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.character.panels.Model as panelModel
import ru.modas.database.character.items.Model as itemModel

object Model: Table("panel_items") {
    val rank =  Model.integer("rank")
    val id_panel = Model.reference("id_panel", panelModel.id_panel)
    val id_item = Model.reference("id_item", itemModel.id_item)

    override val primaryKey = PrimaryKey(Model.id_panel,Model.id_item)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[rank] = dto.rank
                it[id_panel] = dto.id_panel
                it[id_item] = dto.id_item
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
                            rank = it[rank],
                            id_item = it[id_item],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchAll(id_panel: Int): List<DRO>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.id_panel eq id_panel }
                        .map {
                            DRO(
                                rank = it[Model.rank],
                                id_item = it[Model.id_item]
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