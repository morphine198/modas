package ru.modas.features.character.get.user_character

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val login: String,
)

@Serializable
data class DataResponse(
    val DTOList: MutableList<MutableMap<Int, String>>,
)
