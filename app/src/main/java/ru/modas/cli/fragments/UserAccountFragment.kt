package ru.modas.cli.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import ru.modas.cli.R
import ru.modas.cli.activities.LoginActivity

class UserAccountFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAccept: Button
    private lateinit var btnChangeTheme: Button
    private lateinit var btnLogout: Button

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_LOGIN = "user_login"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_THEME = "app_theme"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initSharedPreferences()
        loadUserData()
        setupClickListeners()
    }

    private fun initViews(view: View) {
        etEmail = view.findViewById(R.id.etEmail)
        etLogin = view.findViewById(R.id.etLogin)
        etPassword = view.findViewById(R.id.etPassword)
        btnAccept = view.findViewById(R.id.btnAccept)
        btnChangeTheme = view.findViewById(R.id.btnChangeTheme)
        btnLogout = view.findViewById(R.id.btnLogout)
    }

    private fun initSharedPreferences() {
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun loadUserData() {
        val email = sharedPreferences.getString(KEY_EMAIL, "user@example.com")
        val login = sharedPreferences.getString(KEY_LOGIN, "username")
        val password = sharedPreferences.getString(KEY_PASSWORD, "password123")

        etEmail.setText(email)
        etLogin.setText(login)
        etPassword.setText(password)
    }

    private fun setupClickListeners() {
        btnAccept.setOnClickListener {
            saveUserData()
        }

        btnChangeTheme.setOnClickListener {
            showThemeDialog()
        }

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun saveUserData() {
        val email = etEmail.text.toString().trim()
        val login = etLogin.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Введите почту"
            return
        }

        if (login.isEmpty()) {
            etLogin.error = "Введите логин"
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Введите пароль"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Введите корректный email"
            return
        }

        if (password.length < 6) {
            etPassword.error = "Пароль должен содержать минимум 6 символов"
            return
        }

        sharedPreferences.edit {
            putString(KEY_EMAIL, email)
            putString(KEY_LOGIN, login)
            putString(KEY_PASSWORD, password)
        }

        Toast.makeText(requireContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show()

        etEmail.clearFocus()
        etLogin.clearFocus()
        etPassword.clearFocus()
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Светлая тема", "Тёмная тема", "Системная тема")
        val currentTheme = sharedPreferences.getInt(KEY_THEME, 2)

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите тему")
            .setSingleChoiceItems(themes, currentTheme) { _, which ->
                saveThemePreference(which)
                applyTheme(which)
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveThemePreference(themeIndex: Int) {
        sharedPreferences.edit {
            putInt(KEY_THEME, themeIndex)
        }
    }

    private fun applyTheme(themeIndex: Int) {
        val mode = when (themeIndex) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO      // Светлая
            1 -> AppCompatDelegate.MODE_NIGHT_YES     // Тёмная
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM  // Системная
        }

        // Устанавливаем режим
        AppCompatDelegate.setDefaultNightMode(mode)

        // Пересоздаём activity для немедленного применения темы
        activity?.recreate()

        Toast.makeText(requireContext(), "Тема изменена", Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выход из аккаунта")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Выйти") { _, _ ->
                logout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun logout() {
        sharedPreferences.edit {
            clear()
        }

        Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        activity?.finish()
    }
}