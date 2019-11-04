package com.bramgoedvriend.glaswerk.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bramgoedvriend.glaswerk.MainActivity
import com.bramgoedvriend.glaswerk.R
import com.bramgoedvriend.glaswerk.domain.User

class LoginActivity : AppCompatActivity() {

    private lateinit var loginRepository: LoginRepository
    private val _loginForm = MutableLiveData<LoginFormState>()

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginRepository = LoginRepository(LoginDataSource(), this)

        if (loginRepository.isLoggedIn()) {
            goToMainActivity()
            finish()
        }

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        loading = findViewById(R.id.loading)

        login.setOnClickListener {
            login()
        }

        username.afterTextChanged {
            loginDataChanged(
                username.text.toString()
            )
        }

        password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    login()
            }
            false
        }


    }

    private fun goToMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }

    private fun updateUiWithUser(model: User) {
        val welcome = getString(R.string.welcome)
        val name = model.firstname + " " + model.lastName
        Toast.makeText(
            applicationContext,
            "$welcome $name",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun login() {
        loading.visibility = View.VISIBLE
        val loginResult = loginRepository.login(username.text.toString(), password.text.toString())

        loading.visibility = View.GONE
        if (loginResult is Result.Error) {
            showLoginFailed(loginResult.exception.message.toString())
        }

        if (loginResult is Result.Success) {
            goToMainActivity()
            updateUiWithUser(loginResult.data)
            finish()
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
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
