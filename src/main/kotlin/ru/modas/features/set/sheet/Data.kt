package ru.modas.features.set.sheet

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val id_template: Int,
    val name: String,
    val token: String,
    val sheet: MutableMap<Int, MutableMap<Int, MutableList<String>>>,
)

//@Serializable
//data class DataResponse(
//    val sheet: MutableMap<Int, MutableMap<Int, MutableList<String>>>,
//)
