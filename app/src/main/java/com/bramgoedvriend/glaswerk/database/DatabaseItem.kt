package com.bramgoedvriend.glaswerk.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.bramgoedvriend.glaswerk.domain.Item

@Entity
data class DatabaseItem constructor(
    @PrimaryKey
    val itemid: Int,
    val lokaal_id: Int,
    val naam: String,
    val aantal: Int,
    val min_aantal: Int,
    val max_aantal: Int,
    val bestel_hoeveelheid: Int
)

fun List<DatabaseItem>.asDomainModel(): List<Item> {
    return map {
        Item (
            id =  it.itemid,
            roomId = it.lokaal_id,
            name = it.naam,
            amount = it.aantal,
            minAmount = it.min_aantal,
            maxAmount = it.max_aantal,
            orderAmount = it.bestel_hoeveelheid,
            roomName = null
        )
    }
}

data class DatabaseItemRoom (
    val itemid: Int,
    val lokaal_id: Int,
    val naam: String,
    val aantal: Int,
    val min_aantal: Int,
    val max_aantal: Int,
    val bestel_hoeveelheid: Int,
    val roomId: Int,
    val name: String
)

fun List<DatabaseItemRoom>.asDomainModelItemRoom(): List<Item> {
    return map {
        Item (
            id =  it.itemid,
            roomId = it.lokaal_id,
            name = it.naam,
            amount = it.aantal,
            minAmount = it.min_aantal,
            maxAmount = it.max_aantal,
            orderAmount = it.bestel_hoeveelheid,
            roomName = it.name
        )
    }
}