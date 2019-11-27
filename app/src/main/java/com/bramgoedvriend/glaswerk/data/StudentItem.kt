package com.bramgoedvriend.glaswerk.data

import androidx.room.*

@Entity/*(
    indices = [
    Index("leerlingid"),
    Index("itemid")],
    foreignKeys = [
    ForeignKey(entity = Student::class, parentColumns = ["leerlingid"], childColumns = ["leerlingid"]),
    ForeignKey(entity = Item::class, parentColumns = ["itemid"], childColumns = ["itemid"])])*/
data class StudentItem(
    @PrimaryKey
    @ColumnInfo(name = "leerlingitemid")
    val studentItemId: Int,
    @ColumnInfo(name = "leerlingid")
    val studentId: Int,
    @ColumnInfo(name = "itemid")
    val itemId: Int,
    val opzettelijk: Int
)