package com.bramgoedvriend.glaswerk.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey
    @ColumnInfo(name = "leerlingid")
    val leerlingId: Int,
    @ColumnInfo(name = "klasid")
    val klasId: Int,
    val voornaam: String,
    val achternaam: String
)
