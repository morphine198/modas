package ru.modas.features.create

import kotlinx.serialization.Serializable

@Serializable
data class DataReceive(
    val token: String,
    val experience: Int,
    val character_name: String,
    val class_name: String,
    val origin_name: String,
    val race_name: String,
    val worldview: String
)