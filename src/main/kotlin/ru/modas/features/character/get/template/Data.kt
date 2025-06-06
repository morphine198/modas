package ru.modas.features.character.get.template

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
)

@Serializable
data class DataResponse(
    val DTOList: MutableMap<String, MutableMap<Int?, String?>>,
)
