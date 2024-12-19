package ru.modas.cli.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.modas.cli.R
import ru.modas.cli.presentation.view.MainActivity
import ru.modas.cli.presentation.vm.RegistrationViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etMail: EditText
    private lateinit var etPass: EditText

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)

        observeViewModel()
    }

    private fun observeViewModel() {
        // Навигация в MainActivity после успешной регистрации
        registrationViewModel.navigateToMain.observe(this, Observer { navigate ->
            if (navigate) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                registrationViewModel.onNavigatedToMain()
            }
        })

        // Навигация в LoginActivity
        registrationViewModel.navigateToLogin.observe(this, Observer { navigate ->
            if (navigate) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                registrationViewModel.onNavigatedToLogin()
            }
        })
    }

    fun signInClick(view: View?) {
        registrationViewModel.onSignInClicked()
    }

    fun signUpClick(view: View?) {
        val email = etMail.text.toString()
        val password = etPass.text.toString()
        registrationViewModel.onSignUpClicked(email, password)
    }
}
