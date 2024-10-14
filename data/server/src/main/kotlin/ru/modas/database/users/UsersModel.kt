package ru.modas.database.users

import org.jetbrains.exposed.sql.Table

object UsersModel: Table("users") {
    private val login = UsersModel.varchar("login", 32);
    private val password = UsersModel.varchar("password", 256);
    private val email = UsersModel.varchar("email", 128);
}