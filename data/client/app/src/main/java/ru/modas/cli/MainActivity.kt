package ru.modas.cli
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация нижней панели навигации
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // По умолчанию отображаем главный экран с пользователем
        loadFragment(MainFragment())

        // Настройка навигации при нажатии на кнопки нижней панели
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_settings -> loadFragment(SettingsFragment())
                R.id.nav_wiki -> loadFragment(WikiFragment())
                R.id.nav_new_list -> loadFragment(NewListFragment())
            }
            true
        }
    }

    // Функция для загрузки фрагмента
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
