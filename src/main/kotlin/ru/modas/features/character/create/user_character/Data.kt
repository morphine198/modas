package ru.modas.features.character.create.user_character

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val login: String,
    val id_sheet: Int,
    val character_name: String,
)
