package com.Client.Table.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.Client.Table.R

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var email = findViewById<EditText>(R.id.register_email)
        var username = findViewById<EditText>(R.id.register_username)
        var password = findViewById<EditText>(R.id.password)
        var pass_repeat = findViewById<EditText>(R.id.password_repeat)
        var btn_register = findViewById<Button>(R.id.register_btn)

        btn_register.setOnClickListener {
            val email_check = email.text;
            val username_check = username.text;
            val password_check = password.text;
            val pass_repeat_check = pass_repeat.text;

            
        }

    }
}