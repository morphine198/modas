package ru.modas.cli.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.modas.cli.data.LoginRequest
import ru.modas.cli.R
import ru.modas.cli.network.RetrofitClient

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        loginEditText = findViewById<EditText>(R.id.etLogin)
        passwordEditText = findViewById<EditText>(R.id.etPassword)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Обработка кнопки "Авторизоваться"
    fun onLoginClick(view: View?) {

        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(login, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    // Обработка успешного ответа
                    withContext(Dispatchers.Main) {
                        // Переход на MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                } else {
                    // Обработка ошибки авторизации
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Обработка исключения
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@LoginActivity, "Ошибка-исключение авторизации", Toast.LENGTH_SHORT).show()
//                }
                // Временно для тестирования функционала без БД
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        }
    }

    // Обработка кнопки "Зарегистрироваться"
    fun onRegisterClick(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}