package ru.modas.cli.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.modas.cli.R
import ru.modas.cli.models.CharacterStats

class CharacterStatsFragment : Fragment() {

    private lateinit var tvCharacterName: TextView
    private lateinit var tvLevel: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvProficiencyBonus: TextView
    private lateinit var rvAttributes: RecyclerView
    private lateinit var btnBack: Button
    private lateinit var btnForward: Button

    // Текущий выбранный персонаж (для примера, можно получать из аргументов)
    private var currentCharacterIndex = 0
    private val characters = listOf(
        CharacterStats(
            name = "Арагорн",
            strength = 16,
            dexterity = 14,
            constitution = 15,
            intelligence = 12,
            wisdom = 14,
            charisma = 17,
            level = 8,
            experience = 34000
        ),
        CharacterStats(
            name = "Гэндальф",
            strength = 10,
            dexterity = 12,
            constitution = 13,
            intelligence = 18,
            wisdom = 20,
            charisma = 18,
            level = 15,
            experience = 165000
        ),
        CharacterStats(
            name = "Леголас",
            strength = 12,
            dexterity = 20,
            constitution = 14,
            intelligence = 13,
            wisdom = 15,
            charisma = 16,
            level = 12,
            experience = 100000
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupClickListeners()
        displayCharacterStats(currentCharacterIndex)
    }

    private fun initViews(view: View) {
        tvCharacterName = view.findViewById(R.id.tvCharacterName)
        tvLevel = view.findViewById(R.id.tvLevel)
        tvExperience = view.findViewById(R.id.tvExperience)
        tvProficiencyBonus = view.findViewById(R.id.tvProficiencyBonus)
        rvAttributes = view.findViewById(R.id.rvAttributes)
        btnBack = view.findViewById(R.id.btnBack)
        btnForward = view.findViewById(R.id.btnForward)

        rvAttributes.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            if (currentCharacterIndex > 0) {
                currentCharacterIndex--
                displayCharacterStats(currentCharacterIndex)
            }
        }

        btnForward.setOnClickListener {
            if (currentCharacterIndex < characters.size - 1) {
                currentCharacterIndex++
                displayCharacterStats(currentCharacterIndex)
            }
        }
    }

    private fun displayCharacterStats(index: Int) {
        val character = characters[index]

        tvCharacterName.text = character.name
        tvLevel.text = "Уровень: ${character.level}"
        tvExperience.text = "Опыт: ${character.experience} XP"
        tvProficiencyBonus.text = "Бонус мастерства: +${character.getProficiencyBonus()}"

        // Обновляем состояние кнопок
        btnBack.isEnabled = index > 0
        btnForward.isEnabled = index < characters.size - 1

        // Настройка адаптера для атрибутов
        val attributesList = listOf(
            AttributeItem("Сила (STR)", character.strength, character.getModifier(character.strength)),
            AttributeItem("Ловкость (DEX)", character.dexterity, character.getModifier(character.dexterity)),
            AttributeItem("Телосложение (CON)", character.constitution, character.getModifier(character.constitution)),
            AttributeItem("Интеллект (INT)", character.intelligence, character.getModifier(character.intelligence)),
            AttributeItem("Мудрость (WIS)", character.wisdom, character.getModifier(character.wisdom)),
            AttributeItem("Харизма (CHA)", character.charisma, character.getModifier(character.charisma))
        )

        rvAttributes.adapter = AttributesAdapter(attributesList)
    }

    data class AttributeItem(
        val name: String,
        val value: Int,
        val modifier: Int
    )

    inner class AttributesAdapter(
        private val attributes: List<AttributeItem>
    ) : RecyclerView.Adapter<AttributesAdapter.AttributeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_attribute, parent, false)
            return AttributeViewHolder(view)
        }

        override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
            val attribute = attributes[position]
            holder.bind(attribute)
        }

        override fun getItemCount(): Int = attributes.size

        inner class AttributeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvAttributeName: TextView = itemView.findViewById(R.id.tvAttributeName)
            private val tvAttributeValue: TextView = itemView.findViewById(R.id.tvAttributeValue)
            private val tvAttributeModifier: TextView = itemView.findViewById(R.id.tvAttributeModifier)

            fun bind(attribute: AttributeItem) {
                tvAttributeName.text = attribute.name
                tvAttributeValue.text = attribute.value.toString()
                val modifierText = if (attribute.modifier >= 0) "+${attribute.modifier}" else "${attribute.modifier}"
                tvAttributeModifier.text = "($modifierText)"
            }
        }
    }
}