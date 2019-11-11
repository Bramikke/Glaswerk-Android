package com.bramgoedvriend.glaswerk.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bramgoedvriend.glaswerk.domain.StudentItem

@Entity
data class DatabaseStudentItem constructor(
    @PrimaryKey
    val studentItemId: Int,
    val studentId: Int,
    val itemId: Int,
    val onPurpose: Int
)

fun List<DatabaseStudentItem>.asDomainModel(): List<StudentItem> {
    return map {
        StudentItem (
            studentItemId = it.studentItemId,
            studentId = it.studentId,
            itemId = it.itemId,
            onPurpose = it.onPurpose
        )
    }
}