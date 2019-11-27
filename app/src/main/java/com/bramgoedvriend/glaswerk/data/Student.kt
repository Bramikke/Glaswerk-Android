package com.bramgoedvriend.glaswerk.data

import androidx.room.*

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