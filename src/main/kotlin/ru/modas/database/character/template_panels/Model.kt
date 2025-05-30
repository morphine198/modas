package ru.modas.database.character.template_panels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.character.panels.Model as panelModel
import ru.modas.database.character.templates.Model as templateModel

object Model: Table("template_panels") {
    val rank =  Model.integer("rank")
    val id_panel = Model.reference("id_panel", panelModel.id_panel)
    val id_template = Model.reference("id_template", templateModel.id_template)

    override val primaryKey = PrimaryKey(Model.id_panel,Model.id_template)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[rank] = dto.rank
                it[id_panel] = dto.id_panel
                it[id_template] = dto.id_template
            }
        }
    }

    suspend fun fetch(id_template: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_template eq id_template }.singleOrNull()
                    model?.let {
                        DRO(
                            rank = it[rank],
                            id_panel = it[id_panel],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchAll(id_template: Int): List<DRO>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.id_template eq id_template }
                        .map {
                            DRO(
                                rank = it[Model.rank],
                                id_panel = it[Model.id_panel]
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