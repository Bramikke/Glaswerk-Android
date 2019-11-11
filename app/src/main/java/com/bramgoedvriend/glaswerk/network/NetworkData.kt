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
data class ReduceItem (
    val itemid: Int
):Parcelable

@Parcelize
data class OrderItem(
    val itemid: Int,
    val aantal: Int
):Parcelable

@Parcelize
data class DamageItemNavigate(
    val itemid: Int,
    val itemName: String,
    val itemAmount: Int
):Parcelable