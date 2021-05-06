package com.Client.Table.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.Client.Table.MainViewActivity

import com.Client.Table.R
import com.Client.Table.data.LoginDataSource
import com.Client.Table.data.LoginRepository
import com.Client.Table.network.BackendApi
import com.Client.Table.ui.registration.RegistrationActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    lateinit var myPreference: MyPreference
    lateinit var context: Context

    val languageList = arrayOf("EN","RU")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        context = this
        myPreference = MyPreference(this)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signin = findViewById<Button>(R.id.signinBtn)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val registerBtn = findViewById<Button>(R.id.registerBtn)


        // Responsible for changing language

        // Addapted https://github.com/techguynaresh/androidKotlin

        val selectLanguageBtn = findViewById<Button>(R.id.btnLanguage)
        val selectLanuageSpinner = findViewById<Spinner>(R.id.languageSpinner)

        selectLanuageSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, languageList)

        val lang = myPreference.getLoginCount()
        val index = languageList.indexOf(lang)
        if(index >= 0){
            selectLanuageSpinner.setSelection(index)
        }

        selectLanguageBtn.setOnClickListener {
            myPreference.setLoginCount(languageList[selectLanuageSpinner.selectedItemPosition])
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                //setResult(Activity.RESULT_OK)

                //Complete and destroy login activity once successful
                //finish()
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            signin.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
            registerBtn.setOnClickListener {
                updateUiForRegister()
            }
        }
    }
    private fun updateUiForRegister(){
        val intent = Intent(this, RegistrationActivity::class.java).apply{
//pass stuff?
        }
        startActivity(intent)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        // test authentication for reference purpose
        GlobalScope.launch {
            try {
                println(BackendApi.retrofitService.testauthentication(model.jwtToken).response)
            } catch (e: Exception)
            {
                println("Error: authenticated route not working although logged in")
                e.printStackTrace()
            }
        }

        val intent = Intent(this, MainViewActivity::class.java).apply{
//pass stuff?
        }
        startActivity(intent)
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun attachBaseContext(newBase: Context?) {
        myPreference = MyPreference(newBase!!)
        val lang:String = myPreference.getLoginCount()
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang))
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}