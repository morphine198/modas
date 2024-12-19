package ru.modas.cli.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.modas.cli.R


class CharacterDetailFragment : Fragment() {

    private var character: Character? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейтим разметку фрагмента
        val view = inflater.inflate(R.layout.activity_character_detail_fragment, container, false)

        // Получаем переданные данные о персонаже
        character = arguments?.getSerializable("character_data") as? Character

        // Находим TextView для отображения данных
        val characterNameTextView: TextView = view.findViewById(R.id.characterName)



        // Находим кнопку "Редактировать" и устанавливаем обработчик клика
        val editButton: Button = view.findViewById(R.id.btnEdit)
        editButton.setOnClickListener {
            openCharacterCreaterActivity()
        }

        return view
    }

    // Метод для открытия активности CharacterCreaterActivity
    private fun openCharacterCreaterActivity() {
        val intent = Intent(requireContext(), CharacterCreater::class.java)

        // Передаем данные о персонаже в активность (если нужно)
        character?.let {
            intent.putExtra("character_data", it)
        }

        startActivity(intent)  // Запускаем активность
    }
}
