package com.bramgoedvriend.glaswerk.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bramgoedvriend.glaswerk.domain.Student

@Entity
data class DatabaseStudent constructor(
    @PrimaryKey
    val studentId: Int,
    val classId: Int,
    val firstName: String,
    val lastName: String
)

fun List<DatabaseStudent>.asDomainModel(): List<Student> {
    return map {
        Student (
            studentId = it.studentId,
            classId = it.classId,
            firstName = it.firstName,
            lastName = it.lastName,
            brokenAmount = null
        )
    }
}