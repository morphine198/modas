package ru.modas.features.character.get.sheet

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val id_sheet: Int,
)

@Serializable
data class DataResponse(
    val template_name: String,
)
