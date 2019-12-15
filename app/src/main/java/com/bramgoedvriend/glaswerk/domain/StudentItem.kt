package com.bramgoedvriend.glaswerk.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudentItem(
    @PrimaryKey
    @ColumnInfo(name = "leerlingitemid")
    val studentItemId: Int,
    @ColumnInfo(name = "leerlingid")
    val studentId: Int,
    @ColumnInfo(name = "itemid")
    val itemId: Int,
    val opzettelijk: Int
)
