package com.Client.Table.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.Client.Table.R

class RegistrationActivity : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatPassword: EditText
    val MIN_PASSWORD_LENGTH = 6;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        viewInitializations();
        validateInput();
        performRegistration();
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun viewInitializations() {
        username = findViewById(R.id.register_username)
        email = findViewById(R.id.register_email)
        password = findViewById(R.id.register_password)
        repeatPassword = findViewById(R.id.password_repeat)
    }

    private fun validateInput(): Boolean {
        if (username.getText().toString().equals("")) {
            username.setError("Please Enter Last Name")
            return false
        }
        if (email.getText().toString().equals("")) {
            email.setError("Please Enter Email")
            return false
        }
        if (password.getText().toString().equals("")) {
            password.setError("Please Enter Password")
            return false
        }
        if (repeatPassword.getText().toString().equals("")) {
            repeatPassword.setError("Please Enter Repeat Password")
            return false
        }

        // checking the proper email format
        if (!isEmailValid(email.getText().toString())) {
            email.setError("Please Enter Valid Email")
            return false
        }

        // checking minimum password Length
        if (password.getText().length < MIN_PASSWORD_LENGTH) {
            password.setError("Password Length must be more than " + MIN_PASSWORD_LENGTH + " characters")
            return false
        }

        // Checking if repeat password is same
        if (!password.getText().toString().equals(repeatPassword.text.toString())) {
            repeatPassword.setError("Password does not match")
            return false
        }
        return true
    }

    private fun performRegistration() {
        if (validateInput()) {

            // Input is valid, here send data to your server

            val username = username.getText().toString()
            val email = email.getText().toString()
            val password = password.getText().toString()
            val repeatPassword = repeatPassword.getText().toString()

            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            // Here you can call you API

        }
    }
}