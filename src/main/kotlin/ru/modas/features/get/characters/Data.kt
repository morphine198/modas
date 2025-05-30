package ru.modas.features.get.characters

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val login: String,
)

@Serializable
data class DataResponse(
    val character: MutableMap<Int, String>,
)
