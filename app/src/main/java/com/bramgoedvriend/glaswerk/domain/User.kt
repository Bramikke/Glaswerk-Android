package com.bramgoedvriend.glaswerk.domain

data class User(
    val userid: Int,
    val username: String,
    val firstname: String,
    val lastName: String,
    val password: String
)