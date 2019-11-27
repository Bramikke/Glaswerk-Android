package com.bramgoedvriend.glaswerk.data

import androidx.room.*

@Entity
data class Item(
    @PrimaryKey
    @ColumnInfo(name = "itemid")
    val id: Int,
    @ColumnInfo(name = "lokaal_id")
    val lokaalId: Int,
    val naam: String,
    val aantal: Int,
    @ColumnInfo(name = "min_aantal")
    val minAantal: Int,
    @ColumnInfo(name = "max_aantal")
    val maxAantal: Int,
    @ColumnInfo(name = "bestel_hoeveelheid")
    val bestelHoeveelheid: Int
)