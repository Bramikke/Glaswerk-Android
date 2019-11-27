package com.bramgoedvriend.glaswerk.data

import androidx.room.Embedded
import androidx.room.Relation

data class StudentAndStudentItem(
    @Embedded
    val student: Student,

    @Relation(parentColumn = "leerlingid", entityColumn = "leerlingid")
    val studentItems: List<StudentItem> = emptyList()
)
