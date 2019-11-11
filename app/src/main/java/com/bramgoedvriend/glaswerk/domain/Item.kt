package com.bramgoedvriend.glaswerk.domain

data class Item(
    val id: Int,
    val roomId: Int,
    val name: String,
    val amount: Int,
    val minAmount: Int,
    val maxAmount: Int,
    val orderAmount: Int,

    val roomName: String?
)