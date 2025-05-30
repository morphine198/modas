package ru.modas.features.set.characters

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val login: String,
    val name: String,
    val id_template: Int,
)

//@Serializable
//data class DataResponse(
//    val character: MutableMap<Int, String>,
//)
