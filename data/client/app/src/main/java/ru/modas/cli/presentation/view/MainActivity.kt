package ru.modas.cli.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import ru.modas.cli.R
import ru.modas.cli.presentation.view.fragments.MainFragment
import ru.modas.cli.presentation.vm.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Устанавливаем Toolbar как ActionBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Настройка DrawerLayout и NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        // Настройка кнопки "гамбургера" для открытия бокового меню
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Наблюдаем за выбранным фрагментом в ViewModel
        observeViewModel()

        // Обработка нажатий на элементы бокового меню
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> mainViewModel.onMenuItemSelected(MainViewModel.MenuItem.MAIN)
                R.id.nav_settings -> mainViewModel.onMenuItemSelected(MainViewModel.MenuItem.SETTINGS)
                R.id.nav_wiki -> mainViewModel.onMenuItemSelected(MainViewModel.MenuItem.WIKI)
                R.id.nav_new_list -> mainViewModel.onMenuItemSelected(MainViewModel.MenuItem.NEW_LIST)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun observeViewModel() {
        mainViewModel.currentFragment.observe(this) { fragment ->
            loadFragment(fragment)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // Добавляем в стек для поддержки кнопки "Назад"
            .commit()

        // Включаем или отключаем кнопку "Назад" в зависимости от фрагмента
        supportActionBar?.setDisplayHomeAsUpEnabled(fragment !is MainFragment)
    }

    // Поддержка кнопки "Назад" при открытом боковом меню
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
