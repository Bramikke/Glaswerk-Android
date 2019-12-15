package com.bramgoedvriend.glaswerk.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lokaal(
    @PrimaryKey
    @ColumnInfo(name = "lokaalid")
    val lokaalId: Int,
    val lokaalNaam: String
)
