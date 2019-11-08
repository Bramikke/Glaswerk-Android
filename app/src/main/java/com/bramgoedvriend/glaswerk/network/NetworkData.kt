package com.bramgoedvriend.glaswerk.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentItem(
    val leerlingid: Int ,
    val itemid: Int,
    val opzettelijk: Int
):Parcelable

@Parcelize
data class ReduceItem(
    val itemid: Int,
    val aantal: Int
):Parcelable