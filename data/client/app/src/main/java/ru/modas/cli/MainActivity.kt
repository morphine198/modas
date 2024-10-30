package ru.modas.cli
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // По умолчанию загружаем главный экран
        loadFragment(MainFragment())

        // Настройка нижней панели навигации
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_settings -> loadFragment(SettingsFragment())
                R.id.nav_wiki -> loadFragment(WikiFragment())
                R.id.nav_new_list -> loadFragment(NewListFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // Добавляем фрагменты в стек для поддержки кнопки "Назад"
            .commit()
    }

    // Поддержка кнопки "Назад" в верхнем меню
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Обрабатываем системную кнопку "Назад"
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            // Возвращаемся к предыдущему фрагменту
            supportFragmentManager.popBackStack()
        } else {
            // Если стек пуст, завершаем активность
            finish()
        }
    }
}