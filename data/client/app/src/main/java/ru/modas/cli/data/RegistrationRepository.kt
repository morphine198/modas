package ru.modas.cli.data

class RegistrationRepository {
    fun registerUser(email: String, password: String): Boolean {
        // Реализуйте логику регистрации (например, Firebase, API)
        return email.isNotEmpty() && password.isNotEmpty()
    }
}
