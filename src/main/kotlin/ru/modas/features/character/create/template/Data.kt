package ru.modas.features.character.create.template

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val template_name: String,
    val description: String,
)
