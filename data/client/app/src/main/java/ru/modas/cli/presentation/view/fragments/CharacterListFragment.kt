package ru.modas.cli.presentation.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import ru.modas.cli.R
import ru.modas.cli.presentation.vm.CharacterListModel

class CharacterListFragment : Fragment() {

    private lateinit var viewModel: CharacterListModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[CharacterListModel::class.java]
        val view = inflater.inflate(R.layout.fragment_new_list, container, false)

        // Находим кнопку "Далее" и устанавливаем обработчик клика
        val nextButton: Button = view.findViewById(R.id.btNext1)
        nextButton.setOnClickListener {
            // Логирование для диагностики
            Log.d("CharacterListFragment", "Кнопка 'Далее' нажата")

            // Отправляем сигнал о нажатии кнопки
            viewModel.onNextClicked()
        }

        // Подписка на LiveData для перехода на новый фрагмент
        viewModel.navigateToFragment.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                // Логирование для диагностики
                Log.d("CharacterListFragment", "Навигация активирована")

                openCreateList1Fragment() // Переход на существующий фрагмент
                viewModel.resetNavigation()  // Сбрасываем флаг навигации
            }
        })

        return view
    }

    // Метод для открытия нового фрагмента
    private fun openCreateList1Fragment() {
        // Убедитесь, что фрагмент CreateList_1 уже существует
        val fragment = CreateList_1() // Создаем экземпляр нового фрагмента
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Заменяем текущий фрагмент
            .addToBackStack(null) // Добавляем в стек для поддержки кнопки "Назад"
            .commit()
    }

    // Обрабатываем нажатие кнопки "Назад" в верхнем меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed() // Возвращаемся назад при нажатии на стрелку
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
