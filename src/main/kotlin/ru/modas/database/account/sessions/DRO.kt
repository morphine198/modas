package ru.modas.database.account.sessions

import java.time.Instant

class DRO (
    val token: String,
    val time: Instant,
    val login: String,
)