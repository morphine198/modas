package ru.modas.cli.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R
import ru.modas.cli.presentation.view.MainActivity

class CharacterCreater : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creater)

        // Находим кнопку и устанавливаем обработчик
        val goToListPart1Button: Button = findViewById(R.id.btNext1)
        goToListPart1Button.setOnClickListener {
            // Переход к ListPart1Activity
            openListPart1Activity()
        }
        val gotoMain: Button = findViewById(R.id.btBack1)
            gotoMain.setOnClickListener {
            openMainActivity()
        }
    }

    // Метод для перехода на ListPart1Activity
    private fun openListPart1Activity() {
        val intent = Intent(this, ListPart1::class.java)
        startActivity(intent)
    }
    private fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Закрываем текущую активность
    }
}
