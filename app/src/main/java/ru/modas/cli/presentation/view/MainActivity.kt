package ru.modas.cli.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.modas.cli.R
import ru.modas.cli.presentation.view.fragments.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CharactersListFragment())
                        .commit()
                    //Toast.makeText(this, "Листы", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_wiki -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WikiFragment())
                        .commit()
                    //Toast.makeText(this, "Wiki", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_user -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, UserAccountFragment())
                        .commit()
                    //Toast.makeText(this, "Кабинет", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}