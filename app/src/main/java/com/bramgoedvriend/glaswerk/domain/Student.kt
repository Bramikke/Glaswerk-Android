package com.bramgoedvriend.glaswerk.domain

data class Student(
    val studentId: Int,
    val classId: Int,
    val firstName: String,
    val lastName: String,
    val brokenAmount: Int?
)