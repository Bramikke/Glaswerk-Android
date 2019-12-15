package com.bramgoedvriend.glaswerk.network.DTO

import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student
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

fun NetworkItemsContainer.asDatabaseModel(): Array<Item> {
    return items.map {
        Item(
            id = it.itemid,
            lokaalId = it.lokaal_id,
            naam = it.naam,
            aantal = it.aantal,
            minAantal = it.min_aantal,
            maxAantal = it.max_aantal,
            bestelHoeveelheid = it.bestel_hoeveelheid
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

fun NetworkStudentContainer.asDatabaseModel(): Array<Student> {
    return students.map {
        Student(
            leerlingId = it.leerlingid,
            klasId = it.klasid,
            voornaam = it.voornaam,
            achternaam = it.achternaam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkRoom(
    val lokaalid: Int,
    val naam: String
)

fun NetworkRoomContainer.asDatabaseModel(): Array<Lokaal> {
    return rooms.map {
        Lokaal(
            lokaalId = it.lokaalid,
            lokaalNaam = it.naam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkClass(
    val klasid: Int,
    val naam: String
)

fun NetworkClassContainer.asDatabaseModel(): Array<Klas> {
    return classes.map {
        Klas(
            klasId = it.klasid,
            naam = it.naam
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkStudentItem(
    val leerlingitemid: Int,
    val leerlingid: Int,
    val itemid: Int,
    val opzettelijk: Int
)

fun NetworkStudentItemContainer.asDatabaseModel(): Array<StudentItem> {
    return studentitems.map {
        StudentItem(
            studentItemId = it.leerlingitemid,
            studentId = it.leerlingid,
            itemId = it.itemid,
            opzettelijk = it.opzettelijk
        )
    }.toTypedArray()
}
