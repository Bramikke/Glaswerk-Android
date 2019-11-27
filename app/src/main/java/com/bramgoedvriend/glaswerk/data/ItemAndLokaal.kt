package com.bramgoedvriend.glaswerk.data

import androidx.room.Embedded

data class ItemAndLokaal(
    @Embedded
    val item: Item,
    val lokaalNaam: String
)