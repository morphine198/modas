package ru.modas.utils

import io.ktor.server.application.ApplicationCall
import ru.modas.database.account.sessions.DRO as DRO

class SessionValidator {
    fun CheckToken(dro: DRO): Boolean {
        if (dro != null) return true
        return false
    }
}