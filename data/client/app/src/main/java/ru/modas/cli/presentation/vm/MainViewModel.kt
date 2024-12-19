package ru.modas.cli.presentation.vm

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.modas.cli.presentation.view.fragments.MainFragment
import ru.modas.cli.presentation.view.fragments.SettingsFragment
import ru.modas.cli.presentation.view.fragments.WikiFragment

class MainViewModel : ViewModel() {

    // Определяем возможные элементы меню
    enum class MenuItem {
        MAIN, SETTINGS, WIKI, NEW_LIST
    }

    private val _currentFragment = MutableLiveData<Fragment>(MainFragment()) // По умолчанию главный фрагмент
    val currentFragment: LiveData<Fragment>
        get() = _currentFragment

    // Метод для обработки выбора элемента меню
    fun onMenuItemSelected(menuItem: MenuItem) {
        when (menuItem) {
            MenuItem.MAIN -> _currentFragment.value = MainFragment()
            MenuItem.SETTINGS -> _currentFragment.value = SettingsFragment()
            MenuItem.WIKI -> _currentFragment.value = WikiFragment()
            MenuItem.NEW_LIST -> TODO()
        }
    }
}
