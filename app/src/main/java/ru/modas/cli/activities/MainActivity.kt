package ru.modas.cli.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.modas.cli.R
import ru.modas.cli.fragments.CharactersListFragment
import ru.modas.cli.fragments.UserAccountFragment
import ru.modas.cli.fragments.WikiFragment

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_THEME = "app_theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Загружаем и применяем тему ПЕРЕД super.onCreate
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        applySavedTheme()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Устанавливаем первый фрагмент по умолчанию
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CharactersListFragment())
                .commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CharactersListFragment())
                        .commit()
                    true
                }
                R.id.nav_wiki -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WikiFragment())
                        .commit()
                    true
                }
                R.id.nav_user -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, UserAccountFragment())
                        .commit()
                    true
                }
                else -> false
            }
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
}