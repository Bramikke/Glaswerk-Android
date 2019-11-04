package com.bramgoedvriend.glaswerk.login

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.bramgoedvriend.glaswerk.domain.User

class LoginRepository(val dataSource: LoginDataSource, context: Context) {

    var user: User? = null
        private set

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("loggedIn", false)
    }

    fun logout() {
        user = null
        dataSource.logout()
        sharedPreferences.edit().apply {
            putBoolean("loggedIn", false)
            putString("userId", "")
            putString("displayName", "")
        }.apply()
    }

    fun login(username: String, password: String): Result<User> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(user: User) {
        this.user = user
        sharedPreferences.edit().apply {
            putBoolean("loggedIn", true)
            putInt("userId", user.userid)
            putString("displayName", user.username)
        }.apply()
    }
}
