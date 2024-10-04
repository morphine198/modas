package ru.modas.features.register

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val login: String,
    val password: String,
    val email: String
)

@Serializable
data class DataResponse(
    val token: String
)
