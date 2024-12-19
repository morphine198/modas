package ru.modas.cli.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityVM : ViewModel() {

    // Навигация к MainActivity
    private val _navigateToMain = MutableLiveData(false)
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    // Навигация к RegistrationActivity
    private val _navigateToRegistration = MutableLiveData(false)
    val navigateToRegistration: LiveData<Boolean>
        get() = _navigateToRegistration

    // Обработка нажатия кнопки "Войти"
    fun onLoginClicked() {
        _navigateToMain.value = true
    }

    // Обработка нажатия кнопки "Зарегистрироваться"
    fun onRegisterClicked() {
        _navigateToRegistration.value = true
    }

    // Сбрасываем состояние после перехода к MainActivity
    fun onNavigatedToMain() {
        _navigateToMain.value = false
    }

    // Сбрасываем состояние после перехода к RegistrationActivity
    fun onNavigatedToRegistration() {
        _navigateToRegistration.value = false
    }
}
