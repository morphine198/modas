package ru.modas.database.character.items

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.character.sheets.Model as sheetModel

object Model: Table("items") {
    val id_sheet = reference("id_sheet", sheetModel.id_sheet)
    val id_item = integer("id_item").autoIncrement()
    val rank = integer("rank")
    val collection = integer("collection")
    val type = varchar("type", 128)
    val value = varchar("value", 3072)
    val description = varchar("description", 128)

    override val primaryKey = PrimaryKey(id_sheet, id_item)

    fun insert(dto: DTO): Int? {
        return transaction {
            Model.insert {
                it[id_sheet] = dto.id_sheet
                it[rank] = dto.rank
                it[collection] = dto.collection
                it[type] = dto.type
                it[value] = dto.value
                it[description] = dto.description
            }.resultedValues?.firstOrNull()?.get(id_item)
        }
    }

    fun update(dro: DRO, id_sheet: Int, id_item: Int) {
        transaction {
            Model.update ({ (Model.id_sheet eq id_sheet) and (Model.id_item eq id_item)}) {
                it[rank] = dro.rank
                it[collection] = dro.collection
                it[type] = dro.type
                it[value] = dro.value
                it[description] = dro.description
            }
        }
    }

    suspend fun fetch(id_sheet: Int, id_item: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { (Model.id_sheet eq id_sheet) and (Model.id_item eq id_item)}.singleOrNull()
                    model?.let {
                        DRO(
                            rank = it[Model.rank],
                            collection = it[Model.collection],
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

    suspend fun fetch(id_sheet: Int): List<DTO_I>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.id_sheet eq id_sheet }
                        .map {
                            DTO_I(
                                id_item = it[Model.id_item],
                                rank = it[Model.rank],
                                collection = it[Model.collection],
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