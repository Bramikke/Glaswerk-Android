package com.bramgoedvriend.glaswerk.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bramgoedvriend.glaswerk.domain.Klas

@Entity
data class DatabaseClass constructor(
    @PrimaryKey
    val classId: Int,
    val name: String
)

fun List<DatabaseClass>.asDomainModel(): List<Klas> {
    return map {
        Klas (
            classId = it.classId,
            name = it.name
        )
    }
}