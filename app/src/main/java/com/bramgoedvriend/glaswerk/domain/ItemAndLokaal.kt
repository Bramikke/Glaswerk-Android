package com.bramgoedvriend.glaswerk.domain

import androidx.room.Embedded

data class ItemAndLokaal(
    @Embedded
    val item: Item,
    val lokaalNaam: String
)
