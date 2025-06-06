package ru.modas.features.character.create.item

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val id_sheet: Int,
    val rank: Int,
    val collection: Int,
    val type: String,
    val value: String,
    val description: String,
)

@Serializable
data class DataResponse(
    val id_item: Int?
)
