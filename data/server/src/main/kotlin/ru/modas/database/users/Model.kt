package ru.modas.database.users

import org.jetbrains.exposed.sql.Table

object Model: Table("users") {
    private val login = Model.varchar("login", 32);
    private val password = Model.varchar("password", 256);
    private val email = Model.varchar("email", 128);
}