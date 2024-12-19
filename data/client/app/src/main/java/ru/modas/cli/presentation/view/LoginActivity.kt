package ru.modas.cli.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R
import ru.modas.cli.vm.LoginActivityVM

class LoginActivity : AppCompatActivity() {
    val loginActivityVM : LoginActivityVM = LoginActivityVM()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


    }

    fun signUpClick(view: View?) {
        loginActivityVM.signUp(this);
    }

    fun signInClick(view: View?) {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}