package ru.modas.cli.presentation.view.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R
import ru.modas.cli.presentation.view.MainActivity

class ListPart2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_part2)

        // Находим кнопку "Готово"
        val doneButton: Button = findViewById(R.id.btDone)

        // Устанавливаем обработчик клика по кнопке "Готово"
        doneButton.setOnClickListener {
            // Переход на MainActivity
            openMainActivity()
        }
        val napflButton: Button = findViewById(R.id.btBack3)
        napflButton.setOnClickListener {
            openListPart1Activity()
        }
    }

    // Метод для перехода на MainActivity
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)  // Создаем Intent для перехода на MainActivity
        startActivity(intent)  // Запускаем MainActivity
        finish()  // Закрываем текущую активность
    }
    private fun openListPart1Activity() {
        val intent = Intent(this, ListPart1::class.java)
        startActivity(intent)
        finish()  // Закрываем текущую активность
    }

}
