package com.bramgoedvriend.glaswerk.ui.login

import com.bramgoedvriend.glaswerk.domain.User

data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)
