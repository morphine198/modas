package ru.modas.cli.data

data class LoginRequest(
    val login: String,
    val password: String
)

data class LoginResponse(
    val token: String,
)