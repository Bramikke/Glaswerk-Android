package com.bramgoedvriend.glaswerk.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Klas(
    @PrimaryKey
    @ColumnInfo(name = "klasid")
    val klasId: Int,
    val naam: String
)
