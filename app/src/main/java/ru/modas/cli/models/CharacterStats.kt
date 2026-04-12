package ru.modas.cli.models

data class CharacterStats(
    val name: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val level: Int = 1,
    val experience: Int = 0
) {
    // Модификаторы атрибутов (по правилам D&D 5e)
    fun getModifier(attribute: Int): Int {
        return (attribute - 10) / 2
    }

    // Получение бонуса мастерства
    fun getProficiencyBonus(): Int {
        return when {
            level <= 4 -> 2
            level <= 8 -> 3
            level <= 12 -> 4
            level <= 16 -> 5
            else -> 6
        }
    }
}