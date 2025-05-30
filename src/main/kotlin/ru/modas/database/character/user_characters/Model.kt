package ru.modas.database.character.user_characters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modas.database.account.users.Model as userModel
import ru.modas.database.character.templates.Model as templateModel

object Model: Table("user_characters") {
    val name =  Model.varchar("name", 256)
    val login = Model.reference("login", userModel.login)
    val id_template = Model.reference("id_template", templateModel.id_template)

    override val primaryKey = PrimaryKey(Model.login,Model.id_template)

    fun insert(dto: DTO) {
        transaction {
            Model.insert {
                it[name] = dto.name
                it[login] = dto.login
                it[id_template] = dto.id_template
            }
        }
    }

    suspend fun fetch(login: String): DRO? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    val model = Model.selectAll().where { Model.login eq login }.singleOrNull()
                    model?.let {
                        DRO(
                            name = it[name],
                            id_template = it[id_template],
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchAll(login: String): List<DRO>? {
        return withContext(Dispatchers.IO) {
            try {
                transaction {
                    Model.selectAll().where { Model.login eq login }
                        .map {
                            DRO(
                                name = it[Model.name],
                                id_template = it[Model.id_template]
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