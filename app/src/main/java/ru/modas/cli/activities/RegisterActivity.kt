package ru.modas.cli.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.modas.cli.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_THEME = "app_theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Загружаем и применяем тему перед super.onCreate
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        applySavedTheme()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun applySavedTheme() {
        val themeIndex = sharedPreferences.getInt(KEY_THEME, 2) // 2 = системная по умолчанию
        val mode = when (themeIndex) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO      // Светлая тема
            1 -> AppCompatDelegate.MODE_NIGHT_YES     // Тёмная тема
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM  // Системная тема
        }
        AppCompatDelegate.setDefaultNightMode(mode)
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