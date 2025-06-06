package ru.modas.features.character.update.item

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val id_sheet: Int,
    val id_item: Int,
    val rank: Int,
    val collection: Int,
    val type: String,
    val value: String,
    val description: String,
)
