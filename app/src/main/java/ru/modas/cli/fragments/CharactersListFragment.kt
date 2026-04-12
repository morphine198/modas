package ru.modas.cli.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.modas.cli.R
import ru.modas.cli.adapters.CharacterAdapter
import ru.modas.cli.data.CharacterEntity
import ru.modas.cli.data.CharacterRepository
import ru.modas.cli.viewmodels.CharacterViewModel
import ru.modas.cli.viewmodels.CharacterViewModelFactory

class CharactersListFragment : Fragment() {

    private lateinit var searchAutoCompleteText: AutoCompleteTextView
    private lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderContainer: LinearLayout
    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderTitle: TextView
    private lateinit var placeholderMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var btnAddCharacter: Button

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var historyAdapter: ArrayAdapter<String>

    private lateinit var viewModel: CharacterViewModel

    private var currentQuery = ""
    private var isSearching = false

    companion object {
        private const val PREFS_NAME = "search_prefs"
        private const val KEY_SEARCH_HISTORY = "search_history"
        private const val KEY_LAST_QUERY = "last_query"
        private const val MAX_HISTORY_SIZE = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModel
        val repository = CharacterRepository(requireContext())
        val factory = CharacterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CharacterViewModel::class.java)

        initViews(view)
        initSharedPreferences()
        setupRecyclerView()
        setupHistoryAdapter()
        setupSearchListeners()
        setupAddButton()
        observeCharacters()
        restoreLastQuery()

        // Нужно для автозаполнения персонажами
        lifecycleScope.launch {
            viewModel.addDefaultCharactersIfEmpty()
        }
    }

    private fun initViews(view: View) {
        searchAutoCompleteText = view.findViewById(R.id.searchAutoCompleteText)
        clearButton = view.findViewById(R.id.clearButton)
        clearHistoryButton = view.findViewById(R.id.clearHistoryButton)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerView)
        placeholderContainer = view.findViewById(R.id.placeholderContainer)
        placeholderIcon = view.findViewById(R.id.placeholderIcon)
        placeholderTitle = view.findViewById(R.id.placeholderTitle)
        placeholderMessage = view.findViewById(R.id.placeholderMessage)
        retryButton = view.findViewById(R.id.retryButton)
        btnAddCharacter = view.findViewById(R.id.btnAddCharacter)

        clearHistoryButton.visibility = View.GONE
    }

    private fun initSharedPreferences() {
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        characterAdapter = CharacterAdapter(emptyList()) { characterName ->
            addToSearchHistory(characterName)
            // Переход к экрану характеристик
            val fragment = CharacterStatsFragment()
            val bundle = Bundle().apply {
                putString("character_name", characterName)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = characterAdapter
    }

    private fun observeCharacters() {
        lifecycleScope.launch {
            viewModel.characters.collectLatest { characters ->
                // Обновляем список в адаптере
                if (characters.isNotEmpty()) {
                    val characterNames = characters.map { it.name }
                    characterAdapter.updateItems(characterNames)
                    hidePlaceholder()

                    // Если есть поисковый запрос, фильтруем
                    if (currentQuery.isNotEmpty()) {
                        filterCharacters(currentQuery)
                    }
                } else {
                    characterAdapter.updateItems(emptyList())
                    if (currentQuery.isEmpty()) {
                        showNoResultsPlaceholder("")
                    }
                }
            }
        }
    }

    private fun filterCharacters(query: String) {
        lifecycleScope.launch {
            viewModel.setSearchQuery(query)
        }
    }

    private fun setupHistoryAdapter() {
        historyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )

        searchAutoCompleteText.setAdapter(historyAdapter)
        searchAutoCompleteText.threshold = 0

        searchAutoCompleteText.setOnItemClickListener { _, _, position, _ ->
            val selectedQuery = historyAdapter.getItem(position)
            if (selectedQuery != null) {
                searchAutoCompleteText.setText(selectedQuery)
                searchAutoCompleteText.setSelection(selectedQuery.length)
                performSearch(selectedQuery)
            }
        }
    }

    private fun setupSearchListeners() {
        searchAutoCompleteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString() ?: ""
                clearButton.isVisible = currentQuery.isNotEmpty()

                if (currentQuery.isEmpty()) {
                    // Показываем всех персонажей
                    viewModel.setSearchQuery("")
                    showSearchHistory()
                } else {
                    // Ищем по введенному тексту
                    performSearch(currentQuery)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchAutoCompleteText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(currentQuery)
                true
            } else false
        }

        searchAutoCompleteText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && currentQuery.isEmpty() && !isSearching) {
                showSearchHistory()
            }
        }

        clearButton.setOnClickListener {
            clearSearchText()
        }

        clearHistoryButton.setOnClickListener {
            clearSearchHistory()
        }

        retryButton.setOnClickListener {
            performSearch(currentQuery)
        }
    }

    private fun setupAddButton() {
        btnAddCharacter.setOnClickListener {
            showAddCharacterDialog()
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            viewModel.setSearchQuery("")
            return
        }

        startSearching()
        addToSearchHistory(query)

        // Поиск по базе данных
        lifecycleScope.launch {
            viewModel.setSearchQuery(query)
            stopSearching()
        }
    }

    private fun startSearching() {
        isSearching = true
        progressBar.isVisible = true
        recyclerView.isVisible = false
        placeholderContainer.isVisible = false
    }

    private fun stopSearching() {
        isSearching = false
        progressBar.isVisible = false
        recyclerView.isVisible = true
    }

    private fun hidePlaceholder() {
        placeholderContainer.isVisible = false
        recyclerView.isVisible = true
    }

    private fun showNoResultsPlaceholder(query: String) {
        recyclerView.isVisible = false
        placeholderContainer.isVisible = true

        placeholderIcon.setImageResource(R.drawable.ic_search_empty)
        placeholderTitle.text = "Ничего не найдено"
        placeholderMessage.text = if (query.isNotEmpty()) {
            "По запросу \"$query\" ничего не найдено.\nПопробуйте изменить поисковый запрос."
        } else {
            "Список персонажей пуст.\nНажмите кнопку '+' чтобы добавить персонажа."
        }
        retryButton.isVisible = false
    }

    private fun clearSearchText() {
        searchAutoCompleteText.text.clear()
        currentQuery = ""
        hideKeyboard()
        viewModel.setSearchQuery("")
        searchAutoCompleteText.dismissDropDown()
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            showSearchHistory()
        }, 100)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchAutoCompleteText.windowToken, 0)
    }

    private fun showSearchHistory() {
        val history = getSearchHistory()
        if (history.isNotEmpty() && currentQuery.isEmpty() && !isSearching) {
            historyAdapter.clear()
            historyAdapter.addAll(history)
            historyAdapter.notifyDataSetChanged()

            clearHistoryButton.visibility = View.VISIBLE

            if (searchAutoCompleteText.hasFocus()) {
                searchAutoCompleteText.showDropDown()
            }
        } else {
            clearHistoryButton.visibility = View.GONE
        }
    }

    private fun addToSearchHistory(query: String) {
        if (query.isBlank()) return

        val currentHistory = getSearchHistory().toMutableList()
        currentHistory.remove(query)
        currentHistory.add(0, query)

        while (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.removeAt(currentHistory.size - 1)
        }

        sharedPreferences.edit()
            .putString(KEY_SEARCH_HISTORY, TextUtils.join(",", currentHistory))
            .apply()

        updateHistoryAdapter()
    }

    private fun getSearchHistory(): List<String> {
        val historyString = sharedPreferences.getString(KEY_SEARCH_HISTORY, "")
        return if (!historyString.isNullOrEmpty()) {
            historyString.split(",")
        } else {
            emptyList()
        }
    }

    private fun updateHistoryAdapter() {
        if (!::historyAdapter.isInitialized) return

        val history = getSearchHistory()
        historyAdapter.clear()
        if (history.isNotEmpty()) {
            historyAdapter.addAll(history)
        }
        historyAdapter.notifyDataSetChanged()

        clearHistoryButton.visibility = if (history.isNotEmpty() && currentQuery.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun clearSearchHistory() {
        sharedPreferences.edit().remove(KEY_SEARCH_HISTORY).apply()
        updateHistoryAdapter()
        searchAutoCompleteText.dismissDropDown()
        Toast.makeText(requireContext(), "История поиска очищена", Toast.LENGTH_SHORT).show()
    }

    private fun restoreLastQuery() {
        val lastQuery = sharedPreferences.getString(KEY_LAST_QUERY, "")
        if (!lastQuery.isNullOrEmpty() && lastQuery != "") {
            searchAutoCompleteText.setText(lastQuery)
            currentQuery = lastQuery
            searchAutoCompleteText.setSelection(lastQuery.length)
            if (lastQuery.isNotEmpty()) {
                performSearch(lastQuery)
            }
        }
    }

    private fun showAddCharacterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_character, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCharacterName)
        val etLevel = dialogView.findViewById<EditText>(R.id.etCharacterLevel)
        val etExperience = dialogView.findViewById<EditText>(R.id.etCharacterExperience)

        AlertDialog.Builder(requireContext())
            .setTitle("Добавление персонажа")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = etName.text.toString().trim()
                val level = etLevel.text.toString().toIntOrNull() ?: 1
                val experience = etExperience.text.toString().toIntOrNull() ?: 0

                if (name.isNotBlank()) {
                    val newCharacter = CharacterEntity(
                        name = name,
                        strength = 10,
                        dexterity = 10,
                        constitution = 10,
                        intelligence = 10,
                        wisdom = 10,
                        charisma = 10,
                        level = level,
                        experience = experience
                    )
                    lifecycleScope.launch {
                        viewModel.addCharacter(newCharacter)
                        Toast.makeText(requireContext(), "Персонаж '$name' добавлен", Toast.LENGTH_SHORT).show()

                        // Очищаем поисковый запрос и показываем всех
                        searchAutoCompleteText.text.clear()
                        currentQuery = ""
                        viewModel.setSearchQuery("")
                    }
                } else {
                    Toast.makeText(requireContext(), "Введите имя персонажа", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedPreferences.edit()
            .putString(KEY_LAST_QUERY, currentQuery)
            .apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.edit()
            .putString(KEY_LAST_QUERY, currentQuery)
            .apply()
    }
}