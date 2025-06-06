package ru.modas.features.character.create.sheet

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val template_name: String,
)

@Serializable
data class DataResponse(
    val id_sheet: Int?
)
