package ru.modas.cli.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.modas.cli.R
import ru.modas.cli.data.CharacterEntity
import ru.modas.cli.data.CharacterRepository
import ru.modas.cli.viewmodels.CharacterViewModel
import ru.modas.cli.viewmodels.CharacterViewModelFactory

class CharacterStatsFragment : Fragment() {

    private lateinit var tvCharacterName: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvProficiencyBonus: TextView
    private lateinit var rvAttributes: RecyclerView
    private lateinit var btnBack: Button
    private lateinit var btnForward: Button
    private lateinit var btnEdit: ImageView  // ← ImageView, не Button
    private lateinit var btnDelete: ImageView  // ← ImageView, не Button

    private lateinit var attributesAdapter: AttributesAdapter
    private var characters = listOf<CharacterEntity>()
    private var currentIndex = 0
    private var characterName: String? = null

    private lateinit var viewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_stats, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем имя персонажа из аргументов
        characterName = arguments?.getString("character_name")

        // Инициализация ViewModel
        val repository = CharacterRepository(requireContext())
        val factory = CharacterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CharacterViewModel::class.java)

        initViews(view)
        setupRecyclerView()
        setupClickListeners()
        loadCharacters()
    }

    private fun initViews(view: View) {
        tvCharacterName = view.findViewById(R.id.tvCharacterName)
        tvLevel = view.findViewById(R.id.tvLevel)
        tvExperience = view.findViewById(R.id.tvExperience)
        tvProficiencyBonus = view.findViewById(R.id.tvProficiencyBonus)
        rvAttributes = view.findViewById(R.id.rvAttributes)
        btnBack = view.findViewById(R.id.btnBack)
        btnForward = view.findViewById(R.id.btnForward)
        btnEdit = view.findViewById(R.id.btnEdit)  // ImageView
        btnDelete = view.findViewById(R.id.btnDelete)  // ImageView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        attributesAdapter = AttributesAdapter { attributeName, currentValue ->
            showEditAttributeDialog(attributeName, currentValue)
        }
        rvAttributes.layoutManager = LinearLayoutManager(requireContext())
        rvAttributes.adapter = attributesAdapter
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                displayCharacter(characters[currentIndex])
            }
        }

        btnForward.setOnClickListener {
            if (currentIndex < characters.size - 1) {
                currentIndex++
                displayCharacter(characters[currentIndex])
            }
        }

        btnEdit.setOnClickListener {
            if (characters.isNotEmpty()) {
                showEditCharacterDialog(characters[currentIndex])
            }
        }

        btnDelete.setOnClickListener {
            if (characters.isNotEmpty()) {
                showDeleteConfirmationDialog(characters[currentIndex])
            }
        }
    }

    private fun loadCharacters() {
        lifecycleScope.launch {
            viewModel.characters.collectLatest { characterList ->
                characters = characterList

                if (characters.isNotEmpty()) {
                    // Если передан конкретный персонаж, находим его индекс
                    if (!characterName.isNullOrEmpty()) {
                        val index = characters.indexOfFirst { it.name == characterName }
                        if (index != -1) {
                            currentIndex = index
                        }
                    }

                    // Убеждаемся, что индекс в пределах
                    if (currentIndex >= characters.size) {
                        currentIndex = characters.size - 1
                    }

                    displayCharacter(characters[currentIndex])
                    updateNavigationButtons()
                } else {
                    showEmptyState()
                }
            }
        }
    }

    private fun displayCharacter(character: CharacterEntity) {
        tvCharacterName.text = character.name
        tvLevel.text = "Уровень: ${character.level}"
        tvExperience.text = "Опыт: ${character.experience} XP"
        tvProficiencyBonus.text = "Бонус мастерства: +${getProficiencyBonus(character.level)}"

        val attributes = listOf(
            AttributeItem("Сила (STR)", character.strength, getModifier(character.strength)),
            AttributeItem("Ловкость (DEX)", character.dexterity, getModifier(character.dexterity)),
            AttributeItem("Телосложение (CON)", character.constitution, getModifier(character.constitution)),
            AttributeItem("Интеллект (INT)", character.intelligence, getModifier(character.intelligence)),
            AttributeItem("Мудрость (WIS)", character.wisdom, getModifier(character.wisdom)),
            AttributeItem("Харизма (CHA)", character.charisma, getModifier(character.charisma))
        )

        attributesAdapter.updateAttributes(attributes)
    }

    private fun updateNavigationButtons() {
        btnBack.isEnabled = currentIndex > 0
        btnForward.isEnabled = currentIndex < characters.size - 1
    }

    private fun showEmptyState() {
        tvCharacterName.text = "Нет персонажей"
        tvLevel.text = "Уровень: -"
        tvExperience.text = "Опыт: -"
        tvProficiencyBonus.text = "Бонус мастерства: -"
        attributesAdapter.updateAttributes(emptyList())
        btnBack.isEnabled = false
        btnForward.isEnabled = false
    }

    private fun getModifier(attribute: Int): Int {
        return (attribute - 10) / 2
    }

    private fun getProficiencyBonus(level: Int): Int {
        return when {
            level <= 4 -> 2
            level <= 8 -> 3
            level <= 12 -> 4
            level <= 16 -> 5
            else -> 6
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditAttributeDialog(attributeName: String, currentValue: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_attribute, null)
        val tvAttributeName = dialogView.findViewById<TextView>(R.id.tvDialogAttributeName)
        val seekBar = dialogView.findViewById<SeekBar>(R.id.seekBarAttribute)
        val tvValue = dialogView.findViewById<TextView>(R.id.tvAttributeValue)

        tvAttributeName.text = attributeName
        seekBar.max = 20
        seekBar.min = 3
        seekBar.progress = currentValue - 3
        tvValue.text = currentValue.toString()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress + 3
                tvValue.text = value.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        AlertDialog.Builder(requireContext())
            .setTitle("Изменение атрибута")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newValue = seekBar.progress + 3
                updateCharacterAttribute(attributeName, newValue)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun updateCharacterAttribute(attributeName: String, newValue: Int) {
        if (characters.isEmpty()) return

        val currentCharacter = characters[currentIndex]
        val updatedCharacter = when {
            attributeName.contains("Сила") -> currentCharacter.copy(strength = newValue)
            attributeName.contains("Ловкость") -> currentCharacter.copy(dexterity = newValue)
            attributeName.contains("Телосложение") -> currentCharacter.copy(constitution = newValue)
            attributeName.contains("Интеллект") -> currentCharacter.copy(intelligence = newValue)
            attributeName.contains("Мудрость") -> currentCharacter.copy(wisdom = newValue)
            attributeName.contains("Харизма") -> currentCharacter.copy(charisma = newValue)
            else -> currentCharacter
        }

        lifecycleScope.launch {
            viewModel.updateCharacter(updatedCharacter)
            Toast.makeText(requireContext(), "Атрибут обновлен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditCharacterDialog(character: CharacterEntity) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_character, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCharacterName)
        val etLevel = dialogView.findViewById<EditText>(R.id.etCharacterLevel)
        val etExperience = dialogView.findViewById<EditText>(R.id.etCharacterExperience)

        etName.setText(character.name)
        etLevel.setText(character.level.toString())
        etExperience.setText(character.experience.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Редактирование персонажа")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = etName.text.toString().trim()
                val newLevel = etLevel.text.toString().toIntOrNull() ?: character.level
                val newExperience = etExperience.text.toString().toIntOrNull() ?: character.experience

                if (newName.isNotBlank()) {
                    val updatedCharacter = character.copy(
                        name = newName,
                        level = newLevel,
                        experience = newExperience
                    )

                    lifecycleScope.launch {
                        viewModel.updateCharacter(updatedCharacter)
                        Toast.makeText(requireContext(), "Персонаж обновлен", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(character: CharacterEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление персонажа")
            .setMessage("Вы уверены, что хотите удалить персонажа ${character.name}?")
            .setPositiveButton("Удалить") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteCharacter(character)
                    Toast.makeText(requireContext(), "Персонаж удален", Toast.LENGTH_SHORT).show()

                    // Возвращаемся к списку
                    parentFragmentManager.popBackStack()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    data class AttributeItem(
        val name: String,
        val value: Int,
        val modifier: Int
    )

    inner class AttributesAdapter(
        private val onAttributeClick: (String, Int) -> Unit
    ) : RecyclerView.Adapter<AttributesAdapter.AttributeViewHolder>() {

        private var attributes = listOf<AttributeItem>()

        fun updateAttributes(newAttributes: List<AttributeItem>) {
            attributes = newAttributes
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_attribute, parent, false)
            return AttributeViewHolder(view)
        }

        override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
            val attribute = attributes[position]
            holder.bind(attribute)
            holder.itemView.setOnClickListener {
                onAttributeClick(attribute.name, attribute.value)
            }
        }

        override fun getItemCount(): Int = attributes.size

        inner class AttributeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvAttributeName: TextView = itemView.findViewById(R.id.tvAttributeName)
            private val tvAttributeValue: TextView = itemView.findViewById(R.id.tvAttributeValue)
            private val tvAttributeModifier: TextView = itemView.findViewById(R.id.tvAttributeModifier)
            private val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)

            fun bind(attribute: AttributeItem) {
                tvAttributeName.text = attribute.name
                tvAttributeValue.text = attribute.value.toString()
                val modifierText = if (attribute.modifier >= 0) "+${attribute.modifier}" else "${attribute.modifier}"
                tvAttributeModifier.text = "($modifierText)"

                ivEdit.setOnClickListener {
                    onAttributeClick(attribute.name, attribute.value)
                }
            }
        }
    }
}