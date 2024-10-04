package ru.modas.features.login

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val login: String,
    val password: String
)

@Serializable
data class DataResponse(
    val token: String
)