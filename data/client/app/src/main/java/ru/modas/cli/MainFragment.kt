package ru.modas.cli

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Найти ListView и установить адаптер
        val listView: ListView = view.findViewById(R.id.list_view)
        val items = listOf("List item 1", "List item 2", "List item 3", "List item 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        // Обработка клика по элементу списка
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            // Переход на экран со списком персонажей
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, CharacterListFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
