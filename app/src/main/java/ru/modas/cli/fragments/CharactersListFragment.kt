package ru.modas.cli.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.modas.cli.CharacterAdapter
import ru.modas.cli.R

class CharactersListFragment : Fragment() {

    private lateinit var searchAutoCompleteText: AutoCompleteTextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация элементов
        searchAutoCompleteText = view.findViewById(R.id.searchAutoCompleteText)
        sharedPreferences = requireContext().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        // Загружаем историю поиска и устанавливаем адаптер
        val searchHistory = getSearchHistory()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, searchHistory)
        searchAutoCompleteText.setAdapter(adapter)

        // Создание и инициализация элементов
        val clearButton = view.findViewById<ImageView>(R.id.clearButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        // Слушатель для поля ввода
        searchAutoCompleteText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Показываем кнопку, если есть текст, иначе скрываем
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Очистка поля по нажатию на кнопку
        clearButton.setOnClickListener {
            searchAutoCompleteText.text.clear() // Очистка текста

            // Удаляем историю из SharedPreferences
            sharedPreferences.edit().remove("history").apply()
            // Очищаем адаптер
            val emptyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, emptyList<String>())
            searchAutoCompleteText.setAdapter(emptyAdapter)
            // (Необязательно) Скрыть выпадающий список, если он открыт
            searchAutoCompleteText.dismissDropDown()

            // Скрытие клавиатуры
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchAutoCompleteText.windowToken, 0)
        }

        // Обработка текста в строке поиска
        searchAutoCompleteText.setOnItemClickListener { parent, view, position, id ->
            val query = parent.getItemAtPosition(position).toString()
            // Здесь можно добавить логику для выполнения поиска
        }

        // Сохраняем новый запрос в истории
        searchAutoCompleteText.setOnEditorActionListener { _, actionId, _ ->
            val query = searchAutoCompleteText.text.toString()
            if (!TextUtils.isEmpty(query)) {
                addSearchQuery(query)
            }

            if (query.isNotBlank()) {
                // Показать ProgressBar
                progressBar.visibility = View.VISIBLE
                // Сохраняем в историю
                //addSearchQuery(query)
                // Имитация поиска с задержкой (если нет реального запроса)
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    //searchAutoCompleteText.text.clear()
                    // Здесь можно обновить RecyclerView, если фильтруешь
                }, 1500) // 1.5 секунды "загрузки"
            }

            // Проверяем, что нажата клавиша "Enter"
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Очищаем поле
                searchAutoCompleteText.text.clear()
            }

            false
        }

        // Показываем историю
        searchAutoCompleteText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchAutoCompleteText.adapter.count > 0) {
                searchAutoCompleteText.showDropDown()
            }
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Используем requireContext()
        recyclerView.adapter = CharacterAdapter(listOf("Персонаж 1", "Персонаж 2", "Персонаж 3"))
    }

    // Функция для добавления нового запроса в историю поиска
    private fun addSearchQuery(query: String) {
        // Получаем текущую историю поиска
        val currentHistory = getSearchHistory().toMutableList()
        // Добавляем новый запрос в начало истории
        currentHistory.add(0, query)
        // Ограничиваем количество записей в истории (например, 5)
        if (currentHistory.size > 10) {
            currentHistory.removeAt(currentHistory.size - 1)
        }
        // Сохраняем обновленную историю в SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("history", TextUtils.join(",", currentHistory))
        editor.apply()
        // Обновляем адаптер, чтобы отобразить изменения в истории
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currentHistory)
        searchAutoCompleteText.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        searchAutoCompleteText.showDropDown()
    }

    // Функция для получения истории поиска из SharedPreferences
    private fun getSearchHistory(): List<String> {
        val historyString = sharedPreferences.getString("history", "")
        return if (!historyString.isNullOrEmpty()) {
            historyString.split(",")
        } else {
            emptyList()
        }
    }
}