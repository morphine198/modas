package ru.modas.cli.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    private val _navigateToMain = MutableLiveData(false)
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private val _navigateToLogin = MutableLiveData(false)
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onSignUpClicked(email: String, password: String) {
        // Здесь реализуйте логику регистрации (например, валидацию или обращение к репозиторию)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Успешная регистрация
            _navigateToMain.value = true
        } else {
            // Обработка ошибки (например, через Toast или LiveData для ошибки)
        }
    }

    fun onSignInClicked() {
        _navigateToLogin.value = true
    }

    fun onNavigatedToMain() {
        _navigateToMain.value = false
    }

    fun onNavigatedToLogin() {
        _navigateToLogin.value = false
    }
}
