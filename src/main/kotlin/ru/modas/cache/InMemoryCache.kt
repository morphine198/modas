package ru.modas.cache

import ru.modas.features.register.DataReceive

data class TokenCache(
    val login: String,
    val token: String
)

object InMemoryCache {
    val userList: MutableList<DataReceive> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
}