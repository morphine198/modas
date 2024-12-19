package ru.modas.cli.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharacterListModel : ViewModel() {

    private val _navigateToFragment = MutableLiveData<Boolean>()
    val navigateToFragment: LiveData<Boolean> get() = _navigateToFragment

    fun onNextClicked() {
        // Устанавливаем флаг для перехода на новый фрагмент
        _navigateToFragment.value = true
    }

    // Метод для сброса флага навигации после выполнения перехода
    fun resetNavigation() {
        _navigateToFragment.value = false
    }
}
