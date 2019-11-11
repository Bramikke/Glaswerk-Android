package com.bramgoedvriend.glaswerk.network

import com.bramgoedvriend.glaswerk.database.*
import com.bramgoedvriend.glaswerk.domain.StudentItem
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkItemsContainer(val items: List<NetworkItem>)
@JsonClass(generateAdapter = true)
data class NetworkStudentContainer(val students: List<NetworkStudent>)
@JsonClass(generateAdapter = true)
data class NetworkRoomContainer(val rooms: List<NetworkRoom>)
@JsonClass(generateAdapter = true)
data class NetworkClassContainer(val classes: List<NetworkClass>)
@JsonClass(generateAdapter = true)
data class NetworkStudentItemContainer(val studentitems: List<NetworkStudentItem>)

@JsonClass(generateAdapter = true)
data class NetworkItem(
    val itemid: Int,
    val lokaal_id: Int,
    val naam: String,
    val aantal: Int,
    val min_aantal: Int,
    val max_aantal: Int,
    val bestel_hoeveelheid: Int
)

fun NetworkItemsContainer.asDatabaseModel(): Array<DatabaseItem> {
    return items.map {
        DatabaseItem (
            itemid = it.itemid,
            lokaal_id = it.lokaal_id,
            naam = it.naam,
            aantal = it.aantal,
            min_aantal = it.min_aantal,
            max_aantal = it.max_aantal,
            bestel_hoeveelheid = it.bestel_hoeveelheid
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkStudent(
    val leerlingid: Int,
    val klasid: Int,
    val voornaam: String,
    val achternaam: String
)

fun NetworkStudentContainer.asDatabaseModel(): Array<DatabaseStudent> {
    return students.map {
        DatabaseStudent (
            studentId = it.leerlingid,
            classId = it.klasid,
            firstName = it.voornaam,
            lastName = it.achternaam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkRoom(
    val lokaalid: Int,
    val naam: String
)

fun NetworkRoomContainer.asDatabaseModel(): Array<DatabaseRoom> {
    return rooms.map {
        DatabaseRoom (
            roomId = it.lokaalid,
            name = it.naam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkClass (
    val klasid: Int,
    val naam: String
)

fun NetworkClassContainer.asDatabaseModel(): Array<DatabaseClass> {
    return classes.map {
        DatabaseClass (
            classId = it.klasid,
            name = it.naam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkStudentItem (
    val leerlingitemid: Int,
    val leerlingid: Int,
    val itemid: Int,
    val opzettelijk: Int
)

fun NetworkStudentItemContainer.asDatabaseModel(): Array<DatabaseStudentItem> {
    return studentitems.map {
        DatabaseStudentItem (
            studentItemId = it.leerlingitemid,
            studentId = it.leerlingid,
            itemId = it.itemid,
            onPurpose = it.opzettelijk
        )
    }.toTypedArray()
}

