package ru.modas.cli.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.modas.cli.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Обработка кнопки "Зарегистрироваться"
    fun onRegisterClick(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    // Обработка кнопки "Вернуться"
    fun onBackClick(view: View?) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}