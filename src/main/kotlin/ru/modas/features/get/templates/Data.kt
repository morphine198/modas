package ru.modas.features.get.templates

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val type: String,
)

@Serializable
data class DataResponse(
    val templates: MutableMap<Int, String>,
)
