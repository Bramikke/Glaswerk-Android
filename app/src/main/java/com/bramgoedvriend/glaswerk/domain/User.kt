package com.bramgoedvriend.glaswerk.domain

import java.time.LocalDateTime
import java.util.*

class User(
    val id: Int,
    val name: String,
    val personelNumber: Int?,
    val LastOnline: LocalDateTime?
) {

}