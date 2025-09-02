package ru.modas.cli

data class LoginRequest(
    val login: String,
    val password: String
)

data class LoginResponse(
    val token: String,
)