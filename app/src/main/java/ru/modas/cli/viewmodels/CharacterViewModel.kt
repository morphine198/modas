package ru.modas.cli.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.modas.cli.data.CharacterEntity
import ru.modas.cli.data.CharacterRepository

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val characters = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.getAllCharacters()
        } else {
            repository.searchCharacters(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun addCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.insertCharacter(character)
        }
    }

    fun updateCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.updateCharacter(character)
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.deleteCharacter(character)
        }
    }

    fun addDefaultCharacters() {
        viewModelScope.launch {
            repository.addDefaultCharacters()
        }
    }

    fun addDefaultCharactersIfEmpty() {
        viewModelScope.launch {
            repository.addDefaultCharactersIfEmpty()
        }
    }
}