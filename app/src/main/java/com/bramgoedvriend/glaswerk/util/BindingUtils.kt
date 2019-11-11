package com.bramgoedvriend.glaswerk.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bramgoedvriend.glaswerk.domain.Item
import com.bramgoedvriend.glaswerk.domain.Klas
import com.bramgoedvriend.glaswerk.domain.Lokaal
import com.bramgoedvriend.glaswerk.domain.Student

@BindingAdapter("itemNameString")
fun TextView.setItemNameString(item: Item?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("itemAantalString")
fun TextView.setItemAantalString(item: Item?) {
    item?.let {
        text = item.amount.toString()
    }
}

@BindingAdapter("itemAmountString")
fun TextView.setItemAmountString(item: Item?) {
    item?.let {
        text = item.amount.toString()
    }
}

@BindingAdapter("itemMinString")
fun TextView.setItemMinString(item: Item?) {
    item?.let {
        text = item.minAmount.toString()
    }
}

@BindingAdapter("itemMaxString")
fun TextView.setItemMaxString(item: Item?) {
    item?.let {
        text = item.maxAmount.toString()
    }
}

@BindingAdapter("itemOrderString")
fun TextView.setItemOrderString(item: Item?) {
    item?.let {
        val aantalBestellen = (item.maxAmount - item.amount) / item.orderAmount
        text = String.format("%d x %d", aantalBestellen, item.orderAmount)
    }
}

@BindingAdapter("itemLokaalString")
fun TextView.setItemLokaalString(item: Item?) {
    item?.let {
        text = item.roomName
    }
}

@BindingAdapter("studentNameString")
fun TextView.setStudentNameString(student: Student?) {
    student?.let {
        text = String.format("%s %s", student.firstName, student.lastName)
    }
}

@BindingAdapter("studentNumberBrokenString")
fun TextView.setStudentNumberBrokenString(student: Student?) {
    student?.let {
        text = student.brokenAmount.toString()
    }
}

@BindingAdapter("roomNameString")
fun TextView.setRoomNameString(room: Lokaal?) {
    room?.let {
        text = room.name
    }
}

@BindingAdapter("classNameString")
fun TextView.setClassNameString(klas: Klas?) {
    klas?.let {
        text = klas.name
    }
}