package com.bramgoedvriend.glaswerk.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bramgoedvriend.glaswerk.domain.Lokaal

@Entity
data class DatabaseRoom constructor(
    @PrimaryKey
    val roomId: Int,
    val name: String
)

fun List<DatabaseRoom>.asDomainModel(): List<Lokaal> {
    return map {
        Lokaal (
            roomId = it.roomId,
            name = it.name
        )
    }
}