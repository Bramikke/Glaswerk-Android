package com.bramgoedvriend.glaswerk.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bramgoedvriend.glaswerk.data.*

@BindingAdapter("itemNameString")
fun TextView.setItemNameString(item: Item?) {
    item?.let {
        text = item.naam
    }
}

@BindingAdapter("itemAantalString")
fun TextView.setItemAantalString(item: Item?) {
    item?.let {
        text = item.aantal.toString()
    }
}

@BindingAdapter("itemAmountString")
fun TextView.setItemAmountString(item: Item?) {
    item?.let {
        text = item.aantal.toString()
    }
}

@BindingAdapter("itemMinString")
fun TextView.setItemMinString(item: Item?) {
    item?.let {
        text = item.minAantal.toString()
    }
}

@BindingAdapter("itemMaxString")
fun TextView.setItemMaxString(item: Item?) {
    item?.let {
        text = item.maxAantal.toString()
    }
}

@BindingAdapter("itemOrderString")
fun TextView.setItemOrderString(item: Item?) {
    item?.let {
        val aantalBestellen = (item.maxAantal - item.aantal) / item.bestelHoeveelheid
        text = String.format("%d x %d", aantalBestellen, item.bestelHoeveelheid)
    }
}

@BindingAdapter("studentNameString")
fun TextView.setStudentNameString(student: StudentAndStudentItem?) {
    student?.let {
        text = String.format("%s %s", student.student.voornaam, student.student.achternaam)
    }
}

@BindingAdapter("studentNumberBrokenString")
fun TextView.setStudentNumberBrokenString(student: StudentAndStudentItem?) {
    student?.let {
        text = student.studentItems.count().toString()
    }
}

@BindingAdapter("roomNameString")
fun TextView.setRoomNameString(room: Lokaal?) {
    room?.let {
        text = room.lokaalNaam
    }
}

@BindingAdapter("classNameString")
fun TextView.setClassNameString(klas: Klas?) {
    klas?.let {
        text = klas.naam
    }
}