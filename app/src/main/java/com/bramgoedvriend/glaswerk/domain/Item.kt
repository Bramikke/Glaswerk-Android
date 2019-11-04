package com.bramgoedvriend.glaswerk.domain

data class Item(
    val itemid: Int,
    val lokaalid: Int,
    val naam: String,
    val aantal: Int,
    val min_aantal: Int,
    val max_aantal: Int,
    val bestel_hoeveelheid: Int,
    val lokaal_id: Int?,
    val lokaal_naam: String?
)