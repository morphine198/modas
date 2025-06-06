package ru.modas.database.character.templates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.character.sheets.Model as sheetModel

object Model: Table("templates") {
    val template_name = varchar("template_name", 256)
    val id_sheet = reference("id_sheet", sheetModel.id_sheet).nullable()
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(template_name)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[template_name] = dto.template_name
                it[id_sheet] = dto.id_sheet
                it[description] = dto.description
            }
        }
    }

    fun update(dto: DTO) {
        transaction {
            Model.update ({Model.template_name eq dto.template_name}) {
                it[template_name] = dto.template_name
                it[id_sheet] = dto.id_sheet
                it[description] = dto.description
            }
        }
    }

    suspend fun fetch(template_name: String): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.template_name eq template_name }.singleOrNull()
                    model?.let {
                        DRO(
                            id_sheet = it[id_sheet],
                            description = it[description],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetch(): List<DTO>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll()
                        .map {
                            DTO(
                                template_name = it[template_name],
                                id_sheet = it[id_sheet],
                                description = it[description],
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