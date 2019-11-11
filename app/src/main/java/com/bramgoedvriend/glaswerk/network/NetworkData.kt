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
data class ItemId (
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

@Parcelize
data class ItemNavigate(
    val id: Int,
    val roomId: Int,
    val name: String,
    val amount: Int,
    val minAmount: Int,
    val maxAmount: Int,
    val orderAmount: Int
):Parcelable

@Parcelize
data class Item(
    val itemid: Int?,
    val lokaal_id: Int,
    val naam: String,
    val aantal: Int,
    val min_aantal: Int,
    val max_aantal: Int,
    val bestel_hoeveelheid: Int
):Parcelable