package com.bramgoedvriend.glaswerk.login

import com.bramgoedvriend.glaswerk.domain.User
import java.io.IOException

class LoginDataSource {

    fun login(username: String, password: String): Result<User> {
        try {
            //handle loggedInUser authentication
            val fakeUser = User(1, "Anneke Panneke", "Ann", "Van de Voorde", "123")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        //revoke authentication
    }
}

