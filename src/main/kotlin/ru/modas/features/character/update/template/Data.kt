package ru.modas.features.character.update.template

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val template_name: String,
    val id_sheet: Int,
    val description: String,
)
