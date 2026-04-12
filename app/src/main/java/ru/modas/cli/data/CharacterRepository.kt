package ru.modas.cli.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class CharacterRepository(context: Context) {
    private val characterDao = CharacterDatabase.getDatabase(context).characterDao()

    fun getAllCharacters(): Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

    fun searchCharacters(query: String): Flow<List<CharacterEntity>> = characterDao.searchCharacters(query)

    suspend fun insertCharacter(character: CharacterEntity) {
        characterDao.insertCharacter(character)
    }

    suspend fun updateCharacter(character: CharacterEntity) {
        characterDao.updateCharacter(character)
    }

    suspend fun deleteCharacter(character: CharacterEntity) {
        characterDao.deleteCharacter(character)
    }

    suspend fun addDefaultCharacters() {
        val defaultCharacters = listOf(
            CharacterEntity(
                name = "Арагорн",
                strength = 16,
                dexterity = 14,
                constitution = 15,
                intelligence = 12,
                wisdom = 14,
                charisma = 17,
                level = 8,
                experience = 34000
            ),
            CharacterEntity(
                name = "Гэндальф",
                strength = 10,
                dexterity = 12,
                constitution = 13,
                intelligence = 18,
                wisdom = 20,
                charisma = 18,
                level = 15,
                experience = 165000
            ),
            CharacterEntity(
                name = "Леголас",
                strength = 12,
                dexterity = 20,
                constitution = 14,
                intelligence = 13,
                wisdom = 15,
                charisma = 16,
                level = 12,
                experience = 100000
            ),
            CharacterEntity(
                name = "Фродо",
                strength = 10,
                dexterity = 14,
                constitution = 12,
                intelligence = 15,
                wisdom = 16,
                charisma = 17,
                level = 5,
                experience = 6500
            ),
            CharacterEntity(
                name = "Сэм",
                strength = 13,
                dexterity = 11,
                constitution = 16,
                intelligence = 12,
                wisdom = 14,
                charisma = 15,
                level = 5,
                experience = 6500
            )
        )

        defaultCharacters.forEach { character ->
            characterDao.insertCharacter(character)
        }
    }

    suspend fun addDefaultCharactersIfEmpty() {
        val characters = characterDao.getAllCharacters().firstOrNull()
        if (characters.isNullOrEmpty()) {
            addDefaultCharacters()
        }
    }
}