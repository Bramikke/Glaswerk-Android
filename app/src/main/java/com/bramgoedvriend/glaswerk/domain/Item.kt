package com.bramgoedvriend.glaswerk.domain

import com.squareup.moshi.Json

data class Item(
    @Json(name = "itemid") val id: Int,
    @Json(name = "lokaal_id") val classId: Int,
    @Json(name = "naam") val name: String,
    @Json(name = "aantal") val amount: Int,
    @Json(name = "min_aantal") val minAmount: Int,
    @Json(name = "max_aantal") val maxAmount: Int,
    @Json(name = "bestel_hoeveelheid") val orderAmount: Int,
    @Json(name = "lokaalid") val roomId: Int?,
    @Json(name = "lokaal_naam") val roomName: String?
)