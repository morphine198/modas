package ru.modas.database.character.user_characters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.account.users.Model as userModel

object Model: Table("user_characters") {
    val login = reference("login", userModel.login)
    val id_sheet = integer("id_sheet")
    val character_name = varchar("character_name", 256)


    override val primaryKey = PrimaryKey(login, id_sheet)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[login] = dto.login
                it[id_sheet] = dto.id_sheet
                it[character_name] = dto.character_name
            }
        }
    }

    fun update(dto: DTO) {
        transaction {
            Model.update ({Model.id_sheet eq dto.id_sheet}) {
                it[login] = dto.login
                it[id_sheet] = dto.id_sheet
                it[character_name] = dto.character_name
            }
        }
    }

    suspend fun fetch(login: String, id_sheet: Int): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { (Model.id_sheet eq id_sheet) and (Model.login eq login)}.singleOrNull()
                    model?.let {
                        DRO(
                            character_name = it[Model.character_name],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetch(login: String): List<DRO_L>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.login eq login }
                        .map {
                            DRO_L(
                                id_sheet = it[Model.id_sheet],
                                character_name = it[Model.character_name],
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