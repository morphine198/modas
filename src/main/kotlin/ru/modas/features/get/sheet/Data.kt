package ru.modas.features.get.sheet

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val id_template: Int,
)

@Serializable
data class DataResponse(
    val sheet: MutableMap<Int, MutableMap<Int, MutableList<String>>>,
)
