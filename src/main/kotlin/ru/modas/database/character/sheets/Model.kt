package ru.modas.database.character.sheets

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.character.templates.Model as templateModel

object Model: Table("sheets") {
    val id_sheet = integer("id_sheet").autoIncrement()
    val template_name = templateModel.reference("template_name", templateModel.template_name)

    override val primaryKey = PrimaryKey(id_sheet)

    fun insert(dto: DTO): Int? {
        return transaction {
            Model.insert {
                it[template_name] = dto.template_name
            }.resultedValues?.firstOrNull()?.get(id_sheet)
        }
    }

    suspend fun fetch(id_sheet: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.id_sheet eq id_sheet }.singleOrNull()
                    model?.let {
                        DRO(
                            template_name = it[Model.template_name],
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