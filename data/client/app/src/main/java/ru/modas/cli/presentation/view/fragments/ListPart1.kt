package ru.modas.cli.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R

class ListPart1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_part1)

        // Находим кнопку и устанавливаем обработчик
        val goToListPart2Button: Button = findViewById(R.id.button_next)
        goToListPart2Button.setOnClickListener {
            // Переход к ListPart2Activity
            openListPart2Activity()
        }
        val backButton: Button = findViewById(R.id.btBack2)
        backButton.setOnClickListener {
            openCharacterCreaterActivity()
        }
    }

    // Метод для перехода на ListPart2Activity
    private fun openListPart2Activity() {
        val intent = Intent(this, ListPart2::class.java)
        startActivity(intent)
    }
    // Метод для перехода в CharacterCreaterActivity
    private fun openCharacterCreaterActivity() {
        val intent = Intent(this, CharacterCreater::class.java)
        startActivity(intent)
        finish()  // Закрываем текущую активность
    }

}
