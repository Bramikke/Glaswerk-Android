package com.bramgoedvriend.glaswerk.data

import androidx.room.*

@Entity
data class Lokaal(
    @PrimaryKey
    @ColumnInfo(name = "lokaalid")
    val lokaalId: Int,
    val lokaalNaam: String
)