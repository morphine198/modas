package ru.modas.cli.view


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.modas.cli.R


class RegistrationActivity : AppCompatActivity() {
    var etMail: EditText? = null;
    var etPass: EditText? = null;

    //private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        etMail = findViewById<View>(R.id.etMail) as EditText
        etPass = findViewById<View>(R.id.etPass) as EditText
    }

    fun signInClick(view: View?) {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)


    }
    fun signUpClick(view: View?){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}