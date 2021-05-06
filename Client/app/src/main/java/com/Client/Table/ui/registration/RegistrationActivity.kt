package com.Client.Table.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.Client.Table.R
import androidx.lifecycle.viewModelScope
import com.Client.Table.data.model.User
import com.Client.Table.network.BackendApi
import com.Client.Table.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeatPassword: EditText
    lateinit var submitBtn: Button
    lateinit var check_box: CheckBox
    val MIN_PASSWORD_LENGTH = 6;
    val MIN_USERNAME_LENGTH = 4;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        viewInitializations()

        submitBtn = findViewById(R.id.register_btn)
        submitBtn.setOnClickListener {
            Toast.makeText(this, "Registrating", Toast.LENGTH_SHORT).show()
            submitBtn.visibility = View.VISIBLE
            performRegistration()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun viewInitializations() {
        username = findViewById(R.id.register_username)
        email = findViewById(R.id.register_email)
        password = findViewById(R.id.register_password)
        repeatPassword = findViewById(R.id.password_repeat)
        check_box = findViewById(R.id.register_checkbox)

    }

    private fun validateInput(): Boolean {
        var valid: Boolean = true

        // checking the proper email format
        if (!isEmailValid(email.getText().toString())) {
            email.setError(getString(R.string.emailError))
            valid = false
        }

        if (username.getText().length < MIN_USERNAME_LENGTH) {
            username.setError(getString(R.string.userError))
            valid = false
        }

        // checking minimum password Length
        if (password.getText().length < MIN_PASSWORD_LENGTH) {
            password.setError(getString(R.string.passwordError))
            valid = false
        }

        if (repeatPassword.getText().toString().equals("")) {
            repeatPassword.setError(getString(R.string.repeatError))
            valid = false
        }

        // Checking if repeat password is same
        if (!password.getText().toString().equals(repeatPassword.text.toString())) {
            repeatPassword.setError(getString(R.string.matchError))
            valid = false
        }

        if (!check_box.isChecked()){
            check_box.setError(getString(R.string.checkerror))
            valid = false
        }
        return valid
    }

    private fun performRegistration() {
        if (validateInput()) {

            // Input is valid, here send data to your server

            val username = username.getText().toString()
            val email = email.getText().toString()
            val password = password.getText().toString()
            val repeatPassword = repeatPassword.getText().toString()

            var success: Boolean = false
            runBlocking {
                success = RegistrationModel().register(username, email, password) != null
            }

            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java).apply{}
                startActivity(intent)
            } else {
                Toast.makeText(this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show()
        }
    }
}


