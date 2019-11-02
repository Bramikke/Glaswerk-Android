package com.bramgoedvriend.glaswerk.login

import com.bramgoedvriend.glaswerk.domain.User

data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)
