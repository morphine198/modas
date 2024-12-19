package ru.modas.cli.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R

class CharacterListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character_list, container, false)

        // Включаем поддержку меню в этом фрагменте
        setHasOptionsMenu(true)

        // Устанавливаем поддержку Action Bar с кнопкой "Назад"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return view
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
