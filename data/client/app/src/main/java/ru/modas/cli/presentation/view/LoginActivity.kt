package ru.modas.cli.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.modas.cli.R
import ru.modas.cli.presentation.view.MainActivity
import ru.modas.cli.presentation.vm.LoginActivityVM

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        observeViewModel()
    }

    private fun observeViewModel() {
        // Переход к MainActivity
        loginViewModel.navigateToMain.observe(this, Observer { navigate ->
            if (navigate) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                loginViewModel.onNavigatedToMain()
            }
        })

        // Переход к RegistrationActivity
        loginViewModel.navigateToRegistration.observe(this, Observer { navigate ->
            if (navigate) {
                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
                loginViewModel.onNavigatedToRegistration()
            }
        })
    }

    // Обработка кнопки "Войти"
    fun onLoginClick(view: View?) {
        loginViewModel.onLoginClicked()
    }

    // Обработка кнопки "Зарегистрироваться"
    fun onRegisterClick(view: View?) {
        loginViewModel.onRegisterClicked()
    }
}
