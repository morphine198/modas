package ru.modas.cli

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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    val login = findViewById<EditText>(R.id.etLogin).text.toString()
    val password = findViewById<EditText>(R.id.etPassword).text.toString()

    // Обработка кнопки "Авторизоваться"
    fun onLoginClick(view: View?) {
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
                    Toast.makeText(this@LoginActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Обработка исключения
                Toast.makeText(this@LoginActivity, "Ошибка-исключение авторизации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Обработка кнопки "Зарегистрироваться"
    fun onRegisterClick(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}